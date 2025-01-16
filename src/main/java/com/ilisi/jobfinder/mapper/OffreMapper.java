package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;

public class OffreMapper {

    public static OffreDTO toDto(OffreEmploi offreEmploi) {
        OffreDTO.Entreprise entreprise = OffreDTO.Entreprise.builder()
                .name(offreEmploi.getEntreprise().getNom())
                .id(offreEmploi.getEntreprise().getId())
                .email(offreEmploi.getEntreprise().getEmail())
                .phoneNumber(offreEmploi.getEntreprise().getTelephone()).build();

        OffreDTO dto = new OffreDTO();
        dto.setId(offreEmploi.getId());
        dto.setTitle(offreEmploi.getTitre());
        dto.setDescription(offreEmploi.getDescription());
        dto.setPosition(offreEmploi.getPoste());
        dto.setRequirements(offreEmploi.getExigences());
        dto.setContractType(offreEmploi.getTypeContrat());
        dto.setSalary(offreEmploi.getSalaire());
        dto.setPublicationDate(offreEmploi.getDatePublication());
        dto.setDeadlineDate(offreEmploi.getDateLimite());
        dto.setStatus(offreEmploi.getStatusOffre());
        dto.setQuestion(offreEmploi.getQuestion());
        dto.setCompany(entreprise);
        if (offreEmploi.getAdresse() != null) {
            dto.setAdress(AdresseMapper.toDto(offreEmploi.getAdresse()));
        }
        return dto;
    }

    public static OffreSearchResponseDTO toOffreSearchResponseDTO(OffreEmploi offreEmploi){
        OffreSearchResponseDTO dto = OffreSearchResponseDTO.builder()
                .id(offreEmploi.getId())
                .title(offreEmploi.getTitre())
                .description(offreEmploi.getDescription())
                .position(offreEmploi.getPoste())
                .requirements(offreEmploi.getExigences())
                .contractType(offreEmploi.getTypeContrat())
                .salary(offreEmploi.getSalaire())
                .publicationDate(offreEmploi.getDatePublication())
                .deadlineDate(offreEmploi.getDateLimite())
                .company(EntrepriseMapper.toDto(offreEmploi.getEntreprise()))
                .question(offreEmploi.getQuestion())
                .build();
        if (offreEmploi.getAdresse() != null) {
            dto.setAdress(AdresseMapper.toDto(offreEmploi.getAdresse()));
        }
        return dto;
    }

    public static OffreEmploi toEntity(OffreDTO dto) {
        OffreEmploi offre = new OffreEmploi();
        offre.setTitre(dto.getTitle());
        offre.setDescription(dto.getDescription());
        offre.setPoste(dto.getPosition());
        offre.setExigences(dto.getRequirements());
        offre.setTypeContrat(dto.getContractType());
        offre.setSalaire(dto.getSalary());
        offre.setDatePublication(dto.getPublicationDate());
        offre.setDateLimite(dto.getDeadlineDate());
        offre.setStatusOffre(dto.getStatus());
        offre.setQuestion(dto.getQuestion());
        if (dto.getCompany().getId() != null) {
            Entreprise entreprise = new Entreprise();
            entreprise.setId(dto.getCompany().getId());
            offre.setEntreprise(entreprise);
        }
        if(dto.getAdress() != null){
            offre.setAdresse(AdresseMapper.toEntity(dto.getAdress()));
        }
        return offre;
    }
}
