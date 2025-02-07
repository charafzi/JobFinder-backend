package com.ilisi.jobfinder.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.dto.Notification.FCMTokenRegisterRequest;
import com.ilisi.jobfinder.dto.Notification.NotificationDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.mapper.NotificationMapper;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.NotificationRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public void sendNotificationPostulationCandidatByEntreprise(OffreEmploi offre){
        User user = this.userRepository.findById(offre.getEntreprise().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        String titre= "New Applicant for the " + offre.getPoste() + " Position";
        String contenu = "You have received a new application for the job offer titled :" + offre.getTitre() + ". Check your job offers for more details.";

        // save notification for history
        Map<String, String> data = new HashMap<>();
        data.put("offreId", String.valueOf(offre.getId()));
        com.ilisi.jobfinder.model.Notification saved = this.saveNotificationByUser(user, titre, contenu, data);

        System.out.println("I willl send notif to "+user.getEmail());

        if(user.getFcmToken() != null){
            //send notification by FCM
            if(!this.sendNotificationByFCMToken(user.getFcmToken(),saved.getId(),titre,contenu, data)){
                log.error("Error while sending notification to Entreprise");
            }
        }
    }

    public void sendNotificationStatusChangedCandidature(Candidature candidature, CandidatureStatus status){
        User user = this.userRepository.findById(candidature.getCandidat().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        String titre= "News about your application to "+candidature.getOffreEmploi().getPoste()+" for "+candidature.getOffreEmploi().getEntreprise().getNom();
        String statusString = "";
        switch (status){
            case ACCEPTE:
                statusString+="accepted";
                break;
            case REJETEE:
                statusString+="refused";
                break;
        }

        String contenu= "Your application to "+candidature.getOffreEmploi().getPoste()+" for "+candidature.getOffreEmploi().getEntreprise().getNom()+" has been "+statusString+". Check your applications for more details.";

        // save notification for history
        Map<String, String> data = new HashMap<>();
        data.put("offreId", String.valueOf(candidature.getOffreEmploi().getId()));
        com.ilisi.jobfinder.model.Notification saved = this.saveNotificationByUser(user, titre, contenu, data);

        System.out.println("I willl send notif to "+user.getEmail());

        if(user.getFcmToken() != null){
            //send notification by FCM
            if(!this.sendNotificationByFCMToken(user.getFcmToken(),saved.getId(),titre,contenu, data)){
                log.error("Error while sending notification to Candidat");
            }
        }
    }

    public boolean sendNotificationByFCMToken(String fcmToken,Long id,String title,String body){
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Map<String,String> map = new HashMap<>();
        map.put("id",String.valueOf(id));

        Message message = Message
                .builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putAllData(map)
                .build();

        try {
            firebaseMessaging.send(message);
            return true;
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return false;
        }
    }

    public String testSendNotificationByToken(NotificationMessage notificationMessage){
        Notification notification = Notification
                .builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .build();

        Message message = Message
                .builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        try {
            firebaseMessaging.send(message);
            return "Notification sent successfully !";
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return "Error during sending notification";
        }
    }

    private com.ilisi.jobfinder.model.Notification saveNotificationByUser(User user, String titre, String contenue, Map<String, String> data){
        com.ilisi.jobfinder.model.Notification notification = new com.ilisi.jobfinder.model.Notification();
        notification.setUser(user);
        notification.setTitre(titre);
        notification.setContenu(contenue);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setVue(false);
        notification.setData(data);
        return notificationRepository.save(notification);
    }

    public boolean sendNotificationByFCMToken(String fcmToken, Long id, String title, String body, Map<String, String> data){
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        if (data == null) {
            data = new HashMap<>();
        }
        data.put("id", String.valueOf(id));

        Message message = Message
                .builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            firebaseMessaging.send(message);
            return true;
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
            return false;
        }
    }

    public String registerFCMToken(FCMTokenRegisterRequest request) throws EntityNotFoundException {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        user.setFcmToken(request.getFcmToken());
        this.userRepository.save(user);
        return "FCM token registered successfully !";
    }

    public PageResponse<NotificationDTO> getNotificationsByUser(Long userId, int page, int size) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<com.ilisi.jobfinder.model.Notification> notificationsPage = this.notificationRepository.findNotificationByUserIdOrderByDateEnvoiDesc(userId,pageable);

        Page<NotificationDTO> dtoPage = notificationsPage.map(NotificationMapper::toNotificationDTO);

        return PageResponse.of(dtoPage);

    }

    public String markAsSeen(Long id) {
        com.ilisi.jobfinder.model.Notification notification = this.notificationRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Notification not found"));

        notification.setVue(true);
        this.notificationRepository.save(notification);
        return "Notification marked seen successfully !";
    }

    public String deleteNotificationById(Long id) {
        com.ilisi.jobfinder.model.Notification notification = this.notificationRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Notification not found"));
        this.notificationRepository.delete(notification);
        return "Notification deleted successfully";
    }

    public long unreadCountByUserId(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("User not found"));

        if(user.getNotifications()== null || user.getNotifications().isEmpty()){
            return 0;
        }
        long unreadCount = user.getNotifications().stream().filter(notification -> notification.isVue()==false).count();
        return unreadCount;
    }
}
