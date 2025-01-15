package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.mapper.OffreMapper;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.repository.specification.OffreSpecification;
import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OffreEmploiService {
private final OffreEmploiRepository offremploiRepository;
private final UserRepository userRepository;


    public OffreEmploi create_Offre(OffreEmploi offreEmploi) {
        // Vérifiez si l'ID de l'entreprise est présent dans l'objet
        if (offreEmploi.getEntreprise() == null || offreEmploi.getEntreprise().getId() == 0) {
            throw new RuntimeException("Entreprise ID is required to create an offre.");
        }

        // Récupérez l'utilisateur par son ID
        User user = userRepository.findById(offreEmploi.getEntreprise().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + offreEmploi.getEntreprise().getId()));

        // Vérifiez que l'utilisateur est une entreprise
        if (!(user instanceof Entreprise)) {
            throw new RuntimeException("The provided user ID does not belong to an entreprise.");
        }

        // Associez l'entreprise à l'offre
        offreEmploi.setEntreprise((Entreprise) user);

        // Sauvegardez l'offre
        return offremploiRepository.save(offreEmploi);
    }
    public List<OffreEmploi> getAllOffres(){
        return offremploiRepository.findAll();
    }
    public Optional<OffreEmploi> getOffreById(Long id){
        return offremploiRepository.findById(id);
    }
    public OffreEmploi updateOffre(Long id, OffreEmploi updatedOffre) {
        return offremploiRepository.findById(id).map(offre -> {
            offre.setTitre(updatedOffre.getTitre());
            offre.setDescription(updatedOffre.getDescription());
            offre.setPoste(updatedOffre.getPoste());
            offre.setExigences(updatedOffre.getExigences());
            offre.setTypeContrat(updatedOffre.getTypeContrat());
            offre.setSalaire(updatedOffre.getSalaire());
            offre.setDatePublication(updatedOffre.getDatePublication());
            offre.setDateLimite(updatedOffre.getDateLimite());
            offre.setStatusOffre(updatedOffre.getStatusOffre());
            return offremploiRepository.save(offre);
        }).orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID : " + id));
    }

    public void deleteOffre(Long id){
        //verifier si cette offre existe
        if(offremploiRepository.existsById(id)){
            // si oui supprimez le
            offremploiRepository.deleteById(id);
        }else{
            throw new RuntimeException("Cette Offre d'ID "+id+" n'existe pas !!!");
        }
    }

    public Page<OffreSearchResponseDTO> searchOffres(OffreSearchRequestDTO searchDTO) {
        var spec = OffreSpecification.buildSpecification(searchDTO);

        String sortField = searchDTO.getSortBy().getField();

        Sort sort = Sort.by(Sort.Direction.valueOf(searchDTO.getSortDirection().toString()), sortField);
        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), sort);

        Page<OffreEmploi> offres = offremploiRepository.findAll(spec, pageable);

        Page<OffreSearchResponseDTO> dtos = offres.map(offre -> {
            OffreSearchResponseDTO dto = OffreMapper.toOffreSearchResponseDTO(offre);
            dto.setTimeAgo(this.calculateTimeAgo(dto.getPublicationDate()));
            return dto;
        });

        return dtos;
    }

    public List<OffreDTO> getOffresInRadius(double latitude,double longitude, double radius){
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(longitude,latitude));
        List<OffreEmploi> offreEmplois = this.offremploiRepository.findOffresWithinRadius(point,radius);
        List<OffreDTO> offreDTOS = offreEmplois.stream().map(
                (OffreMapper::toDto)
        ).toList();

        return offreDTOS;
    }

    private String calculateTimeAgo(LocalDateTime datePublication) {
        Duration duration = Duration.between(datePublication, LocalDateTime.now());
        if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }

}
