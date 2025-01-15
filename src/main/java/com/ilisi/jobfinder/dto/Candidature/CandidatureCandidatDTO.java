package com.ilisi.jobfinder.dto.Candidature;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class CandidatureCandidatDTO {
    private OffreDTO offre;
    @Enumerated(EnumType.STRING)
    private CandidatureStatus status;
    private LocalDateTime dateCandidature;
    private Long cvDocId;
    private Long lettreMotivationDocId;
}
