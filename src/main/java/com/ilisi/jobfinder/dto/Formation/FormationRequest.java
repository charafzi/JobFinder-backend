package com.ilisi.jobfinder.dto.Formation;

import com.ilisi.jobfinder.Enum.NiveauEtudeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FormationRequest {
    private String nomEcole;
    private NiveauEtudeStatus niveauEtude;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long candidatId;
}
