package com.ilisi.jobfinder.services.unitTest;

import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.OffreEmploiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OffreServiceUnitTest {
    @Mock
    private OffreEmploiRepository offremploiRepository;
    @Mock
    private UserRepository userRepository;

    private OffreEmploiService offreEmploiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offreEmploiService = new OffreEmploiService(offremploiRepository, userRepository);
    }

    @Test
    void testCreateOffre_Success() {
        // Arrange
        Entreprise entreprise = new Entreprise();
        entreprise.setId(1L);
        
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setEntreprise(entreprise);
        offreEmploi.setTitre("Développeur Java");

        when(userRepository.findById(1L)).thenReturn(Optional.of(entreprise));
        when(offremploiRepository.save(any(OffreEmploi.class))).thenReturn(offreEmploi);

        // Act
        OffreEmploi result = offreEmploiService.create_Offre(offreEmploi);

        // Assert
        assertNotNull(result);
        assertEquals("Développeur Java", result.getTitre());
        verify(offremploiRepository).save(any(OffreEmploi.class));
    }

    @Test
    void testCreateOffre_WithInvalidEntreprise() {
        // Arrange
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setEntreprise(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> offreEmploiService.create_Offre(offreEmploi));
    }

    @Test
    void testGetOffreById_Success() {
        // Arrange
        OffreEmploi offreEmploi = new OffreEmploi();
        offreEmploi.setId(1);
        when(offremploiRepository.findById(1)).thenReturn(Optional.of(offreEmploi));

        // Act
        Optional<OffreEmploi> result = offreEmploiService.getOffreById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testUpdateOffre_Success() {
        // Arrange
        OffreEmploi existingOffre = new OffreEmploi();
        existingOffre.setId(1);
        existingOffre.setTitre("Ancien titre");

        OffreEmploi updatedOffre = new OffreEmploi();
        updatedOffre.setTitre("Nouveau titre");

        when(offremploiRepository.findById(1)).thenReturn(Optional.of(existingOffre));
        when(offremploiRepository.save(any(OffreEmploi.class))).thenReturn(updatedOffre);

        // Act
        OffreEmploi result = offreEmploiService.updateOffre(1, updatedOffre);

        // Assert
        assertNotNull(result);
        assertEquals("Nouveau titre", result.getTitre());
        verify(offremploiRepository).save(any(OffreEmploi.class));
    }

    @Test
    void testDeleteOffre_Success() {
        // Arrange
        when(offremploiRepository.existsById(1)).thenReturn(true);

        // Act
        offreEmploiService.deleteOffre(1);

        // Assert
        verify(offremploiRepository).deleteById(1);
    }

    @Test
    void testDeleteOffre_NotFound() {
        // Arrange
        when(offremploiRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> offreEmploiService.deleteOffre(1));
    }

    @Test
    void testSearchOffres() {
        // Arrange
        OffreSearchRequestDTO searchDTO = new OffreSearchRequestDTO();
        searchDTO.setPage(0);
        searchDTO.setSize(10);

        Entreprise entreprise = new Entreprise();
        entreprise.setId(1L);

        OffreEmploi offre = new OffreEmploi();
        offre.setTitre("Test Offre");
        offre.setDatePublication(LocalDateTime.now());
        offre.setEntreprise(entreprise);

        Page<OffreEmploi> offrePage = new PageImpl<>(Arrays.asList(offre));
        
        when(offremploiRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(offrePage);

        // Act
        Page<?> result = offreEmploiService.searchOffres(searchDTO);

        // Assert
        assertNotNull(result);
        assertFalse(result.getContent().isEmpty());
        verify(offremploiRepository).findAll(any(Specification.class), any(Pageable.class));
    }

}
