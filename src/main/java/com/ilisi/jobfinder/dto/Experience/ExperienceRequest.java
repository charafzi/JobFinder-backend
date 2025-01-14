package com.ilisi.jobfinder.dto.Experience;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExperienceRequest {
    private String poste;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long candidatId;
}
