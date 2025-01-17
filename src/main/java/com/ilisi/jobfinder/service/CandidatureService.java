package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.Candidature.CandidatureCandidatDTO;
import com.ilisi.jobfinder.dto.Candidature.CandidatureDeleteRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureStatusUpdateResquest;
import com.ilisi.jobfinder.dto.Candidature.GetCandidaturesByOffreDTO;
import com.ilisi.jobfinder.dto.CandidatureDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.exceptions.AucuneReponsePourQuestion;
import com.ilisi.jobfinder.exceptions.OffreDejaPostule;
import com.ilisi.jobfinder.mapper.CandidatureMapper;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CandidatureService {
    private final CandidatRepository candidatRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;
    private final DocumentService documentService;


    public void postuler(CandidatureRequest request) throws IOException, OffreDejaPostule,EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(request.getOffreId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        // Check if already applied - using the composite key
        CandidatureId candidatureId = new CandidatureId();
        candidatureId.setCandidatId(candidat.getId());
        candidatureId.setOffreEmploiId(request.getOffreId());

        if (candidatureRepository.findById(candidatureId).isPresent()) {
            throw new OffreDejaPostule();
        }


        Candidature candidature = new Candidature();
        candidature.setId(candidatureId);
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setOffreEmploi(offre);
        candidature.setCandidat(candidat);
        if(offre.getQuestion() != null){
            if(request.getReponse() == null){
                throw new AucuneReponsePourQuestion();
            }
        }

        candidature.setReponse(request.getReponse());


        //Voir si un nouveau CV est téléchargée
        if(request.getNewCv() != null){
            Document newCV = this.documentService.saveDocument(candidat,request.getNewCv(),DocumentType.CV);
            candidature.setCv(newCV);
        }else {
            // get Candidat Cv
            Document cvById = candidat.getDocuments().stream()
                    .filter(document -> document.getId().equals(request.getCvId()))
                    .findFirst()
                    .orElse(null);

            if(cvById == null){
                throw new RuntimeException("Candidat n'a aucun CV avec Id="+request.getCvId());
            }
            candidature.setCv(cvById);
        }

        if(request.getLettreMotivation() != null){
            Document lettreMotivation = this.documentService.saveDocument(candidat,request.getLettreMotivation(),DocumentType.LETTRE_MOTIVATION);
            candidature.setLettreMotivation(lettreMotivation);
        }
        candidature.setStatus(CandidatureStatus.ENVOYEE);
        candidatureRepository.save(candidature);

    }


    public Page<CandidatureDTO> getAllCandidaturesByOffre(GetCandidaturesByOffreDTO searchDTO) {
        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(searchDTO.getOffreId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));
        Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize());

        Page<Candidature> candidatures = this.candidatureRepository.findCandidaturesByOffreEmploiId(searchDTO.getOffreId(), pageable);
        return candidatures.map(CandidatureMapper::toDto);
    }

    public PageResponse<CandidatureCandidatDTO> getAllCandidaturesByUser(Long id, int page, int size) throws EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        log.info("Candidat trouvé avec ID: {}", candidat.getId());

        // Créer un objet Pageable pour la pagination
        Pageable pageable = PageRequest.of(page, size);
        log.info("Recherche des candidatures pour le candidat avec ID: {} et page: {}, taille: {}", candidat.getId(), page, size);

        // Récupérer les candidatures paginées
        Page<Candidature> candidaturesPage = candidatureRepository.findCandidatureByCandidatId(candidat.getId(), pageable);
        log.info("Nombre de candidatures trouvées: {}", candidaturesPage.getTotalElements());

        // Convertir les candidatures en DTOs
        Page<CandidatureCandidatDTO> dtoPage = candidaturesPage.map(CandidatureMapper::toCandidatureCandidatDTO);

        // Créer et retourner la réponse paginée
        return PageResponse.of(dtoPage);
    }

    public void deleteCandidature(CandidatureDeleteRequest request) throws EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        Candidature candidature = candidat.getCandidatures()
                .stream()
                .filter(candid -> candid.getOffreEmploi().getId().equals(request.getOffreId()))
                .findFirst()
                .orElse(null);

        if(candidature == null){
            throw new EntityNotFoundException("Candidature introuvable pour offre ="+request.getOffreId());
        }

        candidat.getCandidatures().remove(candidature);
        candidatRepository.save(candidat);
    }

    public void changeCandidatureStatus(CandidatureStatusUpdateResquest request, CandidatureStatus candidatureStatus) throws EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(request.getOffreId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        // Check if already applied - using the composite key
        CandidatureId candidatureId = new CandidatureId();
        candidatureId.setCandidatId(candidat.getId());
        candidatureId.setOffreEmploiId(request.getOffreId());
        Optional<Candidature> candidature = candidatureRepository.findById(candidatureId);
        if (candidature.isEmpty()) {
            throw new EntityNotFoundException("Candidature introuvable.");
        }
        Candidature candid = candidature.get();

        candid.setStatus(candidatureStatus);
        candidatureRepository.save(candid);
    }

    public boolean checkIfUserApplied(Long userId, Long offreId)  throws EntityNotFoundException{
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(offreId)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        // Check if already applied - using the composite key
        CandidatureId candidatureId = new CandidatureId();
        candidatureId.setCandidatId(userId);
        candidatureId.setOffreEmploiId(offreId);

        if (candidatureRepository.findById(candidatureId).isPresent()) {
            return true;
        }
        return false;
    }
}
