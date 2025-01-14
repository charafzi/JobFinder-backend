package com.ilisi.jobfinder.dto.Candidature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidatureStatusUpdateResquest {
    private Long offreId;
    private String email;
}