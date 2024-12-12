package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auth0Request {
    private String sub;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private boolean email_verified;
    private Role role;

    public Auth0Request(String googleId, String email, String fullName, String profilePicture,Role role) {
        this.setSub(googleId);
        this.setEmail(email);
        this.setName(fullName);
        this.setPicture(profilePicture);
        this.setRole(role);
    }
}
