package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.model.NotificationMessage;
import com.ilisi.jobfinder.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {
    private final NotificationService notificationService;
    @PostMapping("")
    public String sendNotificatio(@RequestBody NotificationMessage notificationMessage){
        return notificationService.sendNotificationByToken(notificationMessage);
    }

}
