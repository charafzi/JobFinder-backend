package com.ilisi.jobfinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationMessage {
    private String recipientToken;
    private String title;
    private String body;
    private Map<String,String> data;
}
