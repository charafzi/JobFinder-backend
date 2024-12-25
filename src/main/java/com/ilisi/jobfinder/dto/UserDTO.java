package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String token;
    private String googleId;
    private Role role;
}
