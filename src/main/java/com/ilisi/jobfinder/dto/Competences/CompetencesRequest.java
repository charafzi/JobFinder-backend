package com.ilisi.jobfinder.dto.Competences;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompetencesRequest {
    private String nomCompetence;
    private Long candidatId;
}
