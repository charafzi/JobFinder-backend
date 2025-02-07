package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.model.SecteurActivite;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Role role = Role.ENTREPRISE;
    private String token;
    private String fcmToken;
    private String refreshToken;
}
