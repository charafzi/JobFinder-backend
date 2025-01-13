package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.model.*;

import java.time.LocalDateTime;

public class CandidatureMapper {

    public static Candidature toCandidature(CandidatureRequest request){
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
    }
}
