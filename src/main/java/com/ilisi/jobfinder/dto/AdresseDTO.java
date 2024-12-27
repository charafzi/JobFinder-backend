package com.ilisi.jobfinder.dto;


import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Builder
@Data
public class AdresseDTO {
    private String ville;
    private String adresse;
    @Nullable
    private Double longitude;
    @Nullable
    private  Double latitude;
}
