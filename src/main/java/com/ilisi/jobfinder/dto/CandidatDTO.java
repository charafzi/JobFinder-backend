package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidatDTO {
    private Long id;
    private String email;
    private String profilePicture;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role = Role.CANDIDAT;
    private String token;
    private String refreshToken;
    private List<Long> cvDocumentsId;
}
