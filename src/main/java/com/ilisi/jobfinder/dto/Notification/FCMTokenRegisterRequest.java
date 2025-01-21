package com.ilisi.jobfinder.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FCMTokenRegisterRequest {
    private Long userId;
    private String fcmToken;
}
