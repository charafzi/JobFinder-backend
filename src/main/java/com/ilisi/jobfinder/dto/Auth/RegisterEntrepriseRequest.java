package com.ilisi.jobfinder.dto.Auth;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.AdresseDTO;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class RegisterEntrepriseRequest {
    private String nom;
    private String phoneNumber;
    private String email;
    private String password;
    private Role role=Role.ENTREPRISE;
    private AdresseDTO adress;
}
