package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class RegisterEntrepriseRequest {
    private String nom;
    private String adresse;
    private String phoneNumber;
    private String email;
    private String password;
    private Role role=Role.ENTREPRISE;
}
