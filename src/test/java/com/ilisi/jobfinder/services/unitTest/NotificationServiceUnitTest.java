package com.ilisi.jobfinder.services.unitTest;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.NotificationRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceUnitTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Entreprise entreprise;
    private OffreEmploi offreEmploi;
    private Candidature candidature;
    private Candidat candidat;
    private com.ilisi.jobfinder.model.Notification notification;

    @BeforeEach
    void setUp() {
        // Setup test data
        entreprise = new Entreprise();
        entreprise.setId(1L);
        entreprise.setEmail("enterprise@test.com");
        entreprise.setRole(Role.ENTREPRISE);
        entreprise.setNotifications(new ArrayList<>());
        entreprise.setFcmToken("test-fcm-token-enterprise");
        entreprise.setNom("Test Enterprise");
        entreprise.setOffres(new ArrayList<>());

        candidat = new Candidat();
        candidat.setId(2L);
        candidat.setEmail("test@test.com");
        candidat.setFcmToken("test-fcm-token");
        candidat.setRole(Role.CANDIDAT);
        candidat.setNotifications(new ArrayList<>());
        candidat.setNom("Test");
        candidat.setPrenom("User");
        candidat.setCandidatures(new ArrayList<>());

        offreEmploi = new OffreEmploi();
        offreEmploi.setId(1L);
        offreEmploi.setTitre("Test Job");
        offreEmploi.setPoste("Developer");
        offreEmploi.setEntreprise(entreprise);

        candidature = new Candidature();
        candidature.setCandidat(candidat);
        candidature.setOffreEmploi(offreEmploi);

        notification = new com.ilisi.jobfinder.model.Notification();
        notification.setId(1L);
        notification.setTitre("Test Notification");
        notification.setContenu("Test Content");
        notification.setDateEnvoi(LocalDateTime.now());
        notification.setVue(false);
        notification.setUser(entreprise);
    }

    @Test
    void sendNotificationPostulationCandidatByEntreprise() throws FirebaseMessagingException {
        // Arrange
        when(userRepository.findById(offreEmploi.getEntreprise().getId()))
                .thenReturn(Optional.of(entreprise));
        when(notificationRepository.save(any(com.ilisi.jobfinder.model.Notification.class)))
                .thenReturn(notification);
        when(firebaseMessaging.send(any(Message.class)))
                .thenReturn("message-id");

        // Act
        notificationService.sendNotificationPostulationCandidatByEntreprise(offreEmploi);

        // Assert
        verify(userRepository).findById(offreEmploi.getEntreprise().getId());
        verify(notificationRepository).save(any(com.ilisi.jobfinder.model.Notification.class));
        verify(firebaseMessaging).send(any(Message.class));
    }

    @Test
    void sendNotificationPostulationCandidatByEntreprise_UserNotFound() {
        // Arrange
        when(userRepository.findById(offreEmploi.getEntreprise().getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            notificationService.sendNotificationPostulationCandidatByEntreprise(offreEmploi)
        );
    }

    @Test
    void sendNotificationStatusChangedCandidature() throws FirebaseMessagingException {
        // Arrange
        when(userRepository.findById(candidature.getCandidat().getId()))
                .thenReturn(Optional.of(candidat));
        when(notificationRepository.save(any(com.ilisi.jobfinder.model.Notification.class)))
                .thenReturn(notification);
        when(firebaseMessaging.send(any(Message.class)))
                .thenReturn("message-id");

        // Act
        notificationService.sendNotificationStatusChangedCandidature(candidature, CandidatureStatus.ACCEPTE);

        // Assert
        verify(userRepository).findById(candidature.getCandidat().getId());
        verify(notificationRepository).save(any(com.ilisi.jobfinder.model.Notification.class));
        verify(firebaseMessaging).send(any(Message.class));
    }

    @Test
    void sendNotificationByFCMToken() throws FirebaseMessagingException {
        // Arrange
        String fcmToken = "test-token";
        Long id = 1L;
        String title = "Test Title";
        String body = "Test Body";
        when(firebaseMessaging.send(any(Message.class)))
                .thenReturn("message-id");

        // Act
        boolean result = notificationService.sendNotificationByFCMToken(fcmToken, id, title, body);

        // Assert
        verify(firebaseMessaging).send(any(Message.class));
        assertTrue(result);
    }

    @Test
    void sendNotificationByFCMToken_FirebaseError() throws FirebaseMessagingException {
        // Arrange
        String fcmToken = "test-token";
        Long id = 1L;
        String title = "Test Title";
        String body = "Test Body";
        when(firebaseMessaging.send(any(Message.class)))
                .thenThrow(FirebaseMessagingException.class);

        // Act
        boolean result = notificationService.sendNotificationByFCMToken(fcmToken, id, title, body);

        // Assert
        verify(firebaseMessaging).send(any(Message.class));
        assertFalse(result);
    }
}
