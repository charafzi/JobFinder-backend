package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.Formation.FormationRequest;
import com.ilisi.jobfinder.dto.Formation.FormationResponse;
import com.ilisi.jobfinder.model.Formation;

public class FormationMapper {

    public static Formation toEntity(FormationRequest request) {
        Formation formation = new Formation();
        formation.setNomEcole(request.getNomEcole());
        formation.setNiveauEtude(request.getNiveauEtude());
        formation.setDateDebut(request.getDateDebut());
        formation.setDateFin(request.getDateFin());
        return formation;
    }

    public static FormationResponse toFormationResponse(Formation formation) {
        return new FormationResponse(
                formation.getId(),
                formation.getNomEcole(),
                formation.getNiveauEtude(),
                formation.getDateDebut(),
                formation.getDateFin(),
                formation.getCandidat().getId()
        );
    }
}
