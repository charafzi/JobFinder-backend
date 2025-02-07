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
    private String picture;
    private String email;
    private boolean email_verified;
    private Role role;

    public Auth0Request(String googleId, String email, String profilePicture, Role role) {
        this.sub = googleId;
        this.email = email;
        this.picture = profilePicture;
        this.role = role;
    }
}
