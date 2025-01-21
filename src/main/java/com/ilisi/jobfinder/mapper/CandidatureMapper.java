package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.dto.Candidature.CandidatureCandidatDTO;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.CandidatureDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.model.*;

import java.time.LocalDateTime;

public class CandidatureMapper {

    /*public static Candidature toCandidature(CandidatureRequest request){
        Candidature candidature=new Candidature();
        candidature.setId(new CandidatureId(request.getCandidatId(), request.getOffreId()));
        if (request.getCandidatId() != null) {
            Candidat candidat = new Candidat();
            candidat.setId(request.getCandidatId());
            candidature.setCandidat(candidat);
        }
        if (request.getOffreId() != null) {
            OffreEmploi offreEmploi = new OffreEmploi();
            offreEmploi.setId(request.getOffreId());
            candidature.setOffreEmploi(offreEmploi);
        }
       candidature.setStatus(CandidatureStatus.ENVOYEE);
        candidature.setDateCandidature(LocalDateTime.now());
        return candidature;
    }*/
    public static CandidatureDTO toDto(Candidature candidature){
        CandidatureDTO.Candidat candidat = null;
        CandidatureDTO.OffreEmploi offreEmploi = null;
        if (candidature.getCandidat() != null) {
            candidat = CandidatureDTO.Candidat.builder()
                    .id(candidature.getCandidat().getId())
                    .email(candidature.getCandidat().getEmail())
                    .firstName(candidature.getCandidat().getPrenom())
                    .lastName(candidature.getCandidat().getNom())
                    .phoneNumber(candidature.getCandidat().getTelephone())
                    .profilePicture(candidature.getCandidat().getPhotoProfile())
                    .build();
        }
        if (candidature.getOffreEmploi() != null) {
            offreEmploi = CandidatureDTO.OffreEmploi.builder()
                    .offreId(candidature.getOffreEmploi().getId())
                    .poste(candidature.getOffreEmploi().getPoste())
                    .ville(candidature.getOffreEmploi().getAdresse() != null
                        ? candidature.getOffreEmploi().getAdresse().getVille()
                            : null)
                    .question(candidature.getOffreEmploi().getQuestion())
                    .build();
        }
        CandidatureDTO candidatureDTO = new CandidatureDTO();
        candidatureDTO.setOffreEmploi(offreEmploi);
        candidatureDTO.setCandidat(candidat);
        candidatureDTO.setCvDocId(candidature.getCv().getId());
        candidatureDTO.setDateCandidature(candidature.getDateCandidature());
        candidatureDTO.setStatus(candidature.getStatus());
        candidatureDTO.setReponse(candidature.getReponse());
        if(candidature.getLettreMotivation() != null){
            candidatureDTO.setLettreMotivationDocId(candidature.getLettreMotivation().getId());
        }
        return candidatureDTO;
    }

    public static CandidatureCandidatDTO toCandidatureCandidatDTO(Candidature candidature){
        OffreDTO offredto = OffreMapper.toDto(candidature.getOffreEmploi());
        return CandidatureCandidatDTO.builder()
                .offre(offredto)
                .dateCandidature(candidature.getDateCandidature())
                .status(candidature.getStatus())
                .cvDocId(candidature.getCv().getId())
                .reponse(candidature.getReponse())
                .lettreMotivationDocId(candidature.getLettreMotivation() != null ? candidature.getLettreMotivation().getId() : null)
                .build();

    }
}
