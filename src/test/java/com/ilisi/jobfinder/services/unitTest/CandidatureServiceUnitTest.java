package com.ilisi.jobfinder.services.unitTest;

import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.exceptions.OffreDejaPostule;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.*;
import com.ilisi.jobfinder.service.CandidatureService;
import com.ilisi.jobfinder.service.DocumentService;
import com.ilisi.jobfinder.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CandidatureServiceUnitTest {

    @Mock
    private CandidatRepository candidatRepository;

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private CandidatureRepository candidatureRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private CandidatureService candidatureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void postuler_Success() throws IOException, OffreDejaPostule {
        // Arrange
        String email = "test@example.com";
        Long offreId = 1L;
        Long cvId = 1L;
        MockMultipartFile newCv = null;
        MockMultipartFile lettreMotivation = null;
        String reponse = "Test response";
        
        CandidatureRequest request = new CandidatureRequest(email, offreId, cvId, newCv, lettreMotivation, reponse);

        Candidat candidat = new Candidat();
        candidat.setEmail(email);
        candidat.setDocuments(new ArrayList<>()); // Initialize the documents list
        
        OffreEmploi offre = new OffreEmploi();
        offre.setId(offreId);
        
        Document cv = new Document();
        cv.setId(cvId);
        candidat.getDocuments().add(cv);

        when(candidatRepository.findByEmail(email)).thenReturn(Optional.of(candidat));
        when(offreEmploiRepository.findById(offreId)).thenReturn(Optional.of(offre));
        when(candidatureRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        candidatureService.postuler(request);

        // Assert
        verify(candidatureRepository).save(any(Candidature.class));
        verify(notificationService).sendNotificationPostulationCandidatByEntreprise(offre);
    }

    @Test
    void postuler_ThrowsException_WhenCandidatNotFound() {
        // Arrange
        String email = "test@example.com";
        Long offreId = 1L;
        CandidatureRequest request = new CandidatureRequest(email, offreId, null, null, null, null);
        
        when(candidatRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> candidatureService.postuler(request));
        verify(candidatureRepository, never()).save(any(Candidature.class));
    }

    @Test
    void postuler_ThrowsException_WhenOffreNotFound() {
        // Arrange
        String email = "test@example.com";
        Long offreId = 1L;
        CandidatureRequest request = new CandidatureRequest(email, offreId, null, null, null, null);

        Candidat candidat = new Candidat();
        candidat.setDocuments(new ArrayList<>()); // Initialize the documents list
        when(candidatRepository.findByEmail(email)).thenReturn(Optional.of(candidat));
        when(offreEmploiRepository.findById(offreId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> candidatureService.postuler(request));
        verify(candidatureRepository, never()).save(any(Candidature.class));
    }
}
