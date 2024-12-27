package com.ilisi.jobfinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String token;
}
