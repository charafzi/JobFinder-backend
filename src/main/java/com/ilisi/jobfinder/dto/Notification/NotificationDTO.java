package com.ilisi.jobfinder.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime sentDate;
    private Map<String,String> data;
    private boolean seen;
}
