package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Candidature.CandidatureCandidatDTO;
import com.ilisi.jobfinder.dto.Notification.FCMTokenRegisterRequest;
import com.ilisi.jobfinder.dto.Notification.NotificationDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.model.NotificationMessage;
import com.ilisi.jobfinder.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public ResponseEntity<PageResponse<NotificationDTO>> getNotificationsByUser(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size
    ){
        try {
            PageResponse<NotificationDTO> result= this.notificationService.getNotificationsByUser(userId,page,size);
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<String> markNotificationSeen(@PathVariable Long id){
        try {
            String response = this.notificationService.markAsSeen(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotificationById(@PathVariable Long id){
        try {
            String response = this.notificationService.deleteNotificationById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/unread-count")
    public ResponseEntity<Long> unreadCountByUserId(@PathVariable Long userId){
        try {
            long count = this.notificationService.unreadCountByUserId(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
