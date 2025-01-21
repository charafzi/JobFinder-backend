package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Notification.FCMTokenRegisterRequest;
import com.ilisi.jobfinder.model.NotificationMessage;
import com.ilisi.jobfinder.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
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
    public String sendNotification(@RequestBody NotificationMessage notificationMessage){
        return notificationService.testSendNotificationByToken(notificationMessage);
    }

    @PostMapping("/register-fcmToken")
    public String registerFCMToken(@RequestBody FCMTokenRegisterRequest fcmTokenRegisterRequest){
        try {
            return notificationService.registerFCMToken(fcmTokenRegisterRequest);
        } catch (EntityNotFoundException e) {
            return "User with id="+fcmTokenRegisterRequest.getUserId()+" not found";
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
