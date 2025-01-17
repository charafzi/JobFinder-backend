package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.model.Adresse;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatureDTO {
    private CandidatureDTO.OffreEmploi offreEmploi;
    private CandidatureDTO.Candidat candidat;
    @Enumerated(EnumType.STRING)
    private CandidatureStatus status;
    private LocalDateTime dateCandidature;
    private Long cvDocId;
    private Long lettreMotivationDocId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Candidat {
        private Long id;
        private String email;
        private String profilePicture;
        private String phoneNumber;
        private String firstName;
        private String lastName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OffreEmploi {
        private Long offreId;
        private String poste;
        private String ville;
    }
}

