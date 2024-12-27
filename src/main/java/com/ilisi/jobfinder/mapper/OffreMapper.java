package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;

public class OffreMapper {

    public static OffreDTO toDto(OffreEmploi offreEmploi) {
        OffreDTO dto = new OffreDTO();
        dto.setTitre(offreEmploi.getTitre());
        dto.setDescription(offreEmploi.getDescription());
        dto.setPoste(offreEmploi.getPoste());
        dto.setExigences(offreEmploi.getExigences());
        dto.setTypeContrat(offreEmploi.getTypeContrat());
        dto.setSalaire(offreEmploi.getSalaire());
        dto.setDatePublication(offreEmploi.getDatePublication());
        dto.setDateLimite(offreEmploi.getDateLimite());
        dto.setStatusOffre(offreEmploi.getStatusOffre());
        dto.setEntrepriseId(offreEmploi.getEntreprise().getId());
        return dto;
    }

    public static OffreSearchResponseDTO toOffreSearchResponseDTO(OffreEmploi offreEmploi){
        return OffreSearchResponseDTO.builder()
                .title(offreEmploi.getTitre())
                .description(offreEmploi.getDescription())
                .position(offreEmploi.getPoste())
                .requirements(offreEmploi.getExigences())
                .contractType(offreEmploi.getTypeContrat())
                .salary(offreEmploi.getSalaire())
                .publicationDate(offreEmploi.getDatePublication())
                .deadlineDate(offreEmploi.getDateLimite())
                .company(EntrepriseMapper.toDto(offreEmploi.getEntreprise()))
                .build();
    }

    public static OffreEmploi toEntity(OffreDTO dto) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(dto.getTitre());
        offre.setDescription(dto.getDescription());
        offre.setPoste(dto.getPoste());
        offre.setExigences(dto.getExigences());
        offre.setTypeContrat(dto.getTypeContrat());
        offre.setSalaire(dto.getSalaire());
        offre.setDatePublication(dto.getDatePublication());
        offre.setDateLimite(dto.getDateLimite());
        offre.setStatusOffre(dto.getStatusOffre());
        if (dto.getEntrepriseId() != null) {
            Entreprise entreprise = new Entreprise();
            entreprise.setId(dto.getEntrepriseId());
            offre.setEntreprise(entreprise);
        }
        return offre;
    }
}
