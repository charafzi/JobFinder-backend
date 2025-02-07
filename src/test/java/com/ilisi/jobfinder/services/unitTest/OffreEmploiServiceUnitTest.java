package com.ilisi.jobfinder.services.unitTest;

import com.ilisi.jobfinder.Enum.StatusOffre;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.OffreEmploiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OffreEmploiServiceUnitTest {

    @Mock
    private OffreEmploiRepository offreEmploiRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OffreEmploiService offreEmploiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_Offre_Success() {
        // Arrange
        Entreprise entreprise = new Entreprise();
        entreprise.setId(1L);
        
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setEntreprise(entreprise);
        offreEmploi.setTitre("Software Developer");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(entreprise));
        when(offreEmploiRepository.save(any(OffreEmploi.class))).thenReturn(offreEmploi);

        // Act
        OffreEmploi result = offreEmploiService.create_Offre(offreEmploi);

        // Assert
        assertNotNull(result);
        assertEquals(StatusOffre.active, result.getStatusOffre());
        assertNotNull(result.getDatePublication());
        verify(offreEmploiRepository).save(any(OffreEmploi.class));
    }

    @Test
    void updateOffre_Success() {
        // Arrange
        Long offreId = 1L;
        OffreEmploi existingOffre = new OffreEmploi();
        existingOffre.setId(offreId);
        existingOffre.setTitre("Old Title");

        OffreEmploi updatedOffre = new OffreEmploi();
        updatedOffre.setTitre("New Title");
        
        when(offreEmploiRepository.findById(offreId)).thenReturn(Optional.of(existingOffre));
        when(offreEmploiRepository.save(any(OffreEmploi.class))).thenReturn(updatedOffre);

        // Act
        OffreEmploi result = offreEmploiService.updateOffre(offreId, updatedOffre);

        // Assert
        assertNotNull(result);
        assertEquals("New Title", result.getTitre());
        verify(offreEmploiRepository).save(any(OffreEmploi.class));
    }

    @Test
    void deleteOffre_Success() {
        // Arrange
        Long offreId = 1L;
        when(offreEmploiRepository.existsById(offreId)).thenReturn(true);

        // Act
        offreEmploiService.deleteOffre(offreId);

        // Assert
        verify(offreEmploiRepository).deleteById(offreId);
    }
}
