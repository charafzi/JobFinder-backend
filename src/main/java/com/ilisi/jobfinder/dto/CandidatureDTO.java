package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatureDTO {
    private CandidatureDTO.Candidat candidat;
    private Long cvDocId;
    private Long lettreMotivationDocId;
    private Long offreId;

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
}
