package com.ilisi.jobfinder.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.dto.Notification.FCMTokenRegisterRequest;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class NotificationService {
    private FirebaseMessaging firebaseMessaging;
    private UserRepository userRepository;

    public void sendNotificationPostulationCandidatByEntreprise(OffreEmploi offre){
        User user = this.userRepository.findById(offre.getEntreprise().getId())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        String titre= "New Applicant for the " + offre.getPoste() + " Position";
        String contenu = "You have received a new application for the job offer titled :" + offre.getTitre() + ". Check your job offers for more details.";

        // save notification for history
        this.saveNotificationByUser(user,titre,contenu);

        System.out.println("I willl send notif to "+user.getEmail());

        if(user.getFcmToken() != null){
            //send notification by FCM
            if(!this.sendNotificationByFCMToken(user.getFcmToken(),titre,contenu)){
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
        this.saveNotificationByUser(user,titre,contenu);

        System.out.println("I willl send notif to "+user.getEmail());

        if(user.getFcmToken() != null){
            //send notification by FCM
            if(!this.sendNotificationByFCMToken(user.getFcmToken(),titre,contenu)){
                log.error("Error while sending notification to Entreprise");
            }
        }
    }

    public boolean sendNotificationByFCMToken(String fcmToken,String title,String body){
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Map<String,String> map = new HashMap<>();
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

    private void saveNotificationByUser(User user, String titre, String contenue){
        com.ilisi.jobfinder.model.Notification notification = new com.ilisi.jobfinder.model.Notification();
        notification.setTitre(titre);
        notification.setContenu(contenue);
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setVue(false);
        user.getNotifications().add(notification);
        userRepository.save(user);
    }



    public String registerFCMToken(FCMTokenRegisterRequest request) throws EntityNotFoundException {
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        user.setFcmToken(request.getFcmToken());
        this.userRepository.save(user);
        return "FCM token registered successfully !";
    }
}
