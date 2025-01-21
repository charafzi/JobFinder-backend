package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.Competences.CompetencesRequest;
import com.ilisi.jobfinder.dto.Competences.CompetencesResponse;
import com.ilisi.jobfinder.model.Competences;

public class CompetencesMapper {
    public static Competences toEntity(CompetencesRequest request) {
        Competences competences = new Competences();
        competences.setNomCompetence(request.getNomCompetence());
        return competences;
    }

    public static CompetencesResponse toCompetencesResponse(Competences competences) {
        return new CompetencesResponse(
            competences.getId(),
            competences.getNomCompetence(),
            competences.getCandidat().getId()
        );
    }
}
