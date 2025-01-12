package com.ilisi.jobfinder.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdresseDTO {
    private String city;
    private String adress;
    @Nullable
    private Double longitude;
    @Nullable
    private  Double latitude;
}
