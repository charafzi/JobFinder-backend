package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.Enum.StatusOffre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OffreDTO {
    private String titre;
    private String description;
    private String poste;
    private List<String> exigences;
    private ContratType typeContrat;
    private double salaire;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datePublication;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateLimite;

    private StatusOffre statusOffre;
    private Long entrepriseId;
}
