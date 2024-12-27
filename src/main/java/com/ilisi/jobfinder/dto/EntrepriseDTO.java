package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.model.SecteurActivite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepriseDTO {
    private Long id;
    private String email;
    private String profilePicture;
    private String phoneNumber;
    private String name;
    private String about;
    private List<SecteurActivite> activitySectors;
    private AdresseDTO adress;
    private String token;
}
