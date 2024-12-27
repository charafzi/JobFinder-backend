package com.ilisi.jobfinder.dto.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
}