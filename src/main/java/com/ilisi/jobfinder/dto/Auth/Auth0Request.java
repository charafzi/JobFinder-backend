package com.ilisi.jobfinder.dto.Auth;

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
    private String email;
    private boolean email_verified;
    private Role role;

    public Auth0Request(String googleId, String email, String fullName, String profilePicture,Role role) {
        this.setSub(googleId);
        this.setEmail(email);
        this.setName(fullName);
        this.setRole(role);
    }
//    public User toUser() {
//        if (this.role == Role.ENTREPRISE) {
//            return Entreprise.builder()
//                    .googleId(this.sub)
//                    .email(this.email)
//                    .nom(this.name)
//                    .role(this.role)
//                    .build();
//        } else {
//            return Candidat.builder()
//                    .googleId(this.sub)
//                    .email(this.email)
//                    .nom(this.name)
//                    .role(this.role)
//                    .build();
//        }
//    }


}
