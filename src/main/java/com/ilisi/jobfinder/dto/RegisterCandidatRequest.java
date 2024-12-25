package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterCandidatRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String photoProfile;
    private String email;
    private String password;
    private Role role=Role.CANDIDAT;
}
