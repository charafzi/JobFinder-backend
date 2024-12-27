package com.ilisi.jobfinder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntrepriseDTO {
    private Long id;
    private String email;
    private String photoProfile;
    private String telephone;
    private String nom;
    private String adresse;
}
