package com.ilisi.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepriseDTO {
    private Long id;
    private String email;
    private String photoProfile;
    private String telephone;
    private String nom;
    private String adresse;
}
