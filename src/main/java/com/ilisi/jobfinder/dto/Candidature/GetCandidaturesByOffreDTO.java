package com.ilisi.jobfinder.dto.Candidature;

import lombok.Builder;
import lombok.Data;
@Data
public class GetCandidaturesByOffreDTO {
    private Long offreId;
    private int page = 0;
    private int size = 10;
}
