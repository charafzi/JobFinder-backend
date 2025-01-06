package com.ilisi.jobfinder.controllers;

import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.Enum.SortBy;
import com.ilisi.jobfinder.Enum.SortDirection;
import com.ilisi.jobfinder.Enum.StatusOffre;
import com.ilisi.jobfinder.controller.OffreController;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.service.OffreEmploiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OffreControllerUnitTest {

    @Mock
    private OffreEmploiService offreEmploiService;

    private OffreController offreController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offreController = new OffreController(offreEmploiService);
    }

    @Test
    void testCreateOffre_Success() {
        // Arrange
        OffreDTO offreDTO = OffreDTO.builder()
                .title("Développeur Java")
                .description("Description du poste")
                .position("Développeur Senior")
                .contractType(ContratType.CDI)
                .salary(50000.0)
                .publicationDate(LocalDateTime.now())
                .deadlineDate(LocalDateTime.now().plusDays(30))
                .status(StatusOffre.active)
                .companyId(1L)
                .build();

        when(offreEmploiService.create_Offre(any(OffreEmploi.class))).thenReturn(new OffreEmploi());

        // Act
        ResponseEntity<Void> response = offreController.createOffre(offreDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(offreEmploiService).create_Offre(any(OffreEmploi.class));
    }

    @Test
    void testCreateOffre_BadRequest() {
        // Arrange
        OffreDTO offreDTO = new OffreDTO();
        when(offreEmploiService.create_Offre(any(OffreEmploi.class)))
                .thenThrow(new RuntimeException("Invalid data"));

        // Act
        ResponseEntity<Void> response = offreController.createOffre(offreDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetAllOffers_Success() {
        Entreprise entreprise = new Entreprise();
        entreprise.setNom("Test company");
        entreprise.setEmail("test@gmail.com");
        OffreEmploi offreEmploi1 = new OffreEmploi();
        OffreEmploi offreEmploi2 = new OffreEmploi();
        offreEmploi1.setEntreprise(entreprise);
        offreEmploi2.setEntreprise(entreprise);
        // Arrange
        List<OffreEmploi> offres = Arrays.asList(
                offreEmploi1, offreEmploi2
        );
        when(offreEmploiService.getAllOffres()).thenReturn(offres);

        // Act
        ResponseEntity<List<OffreDTO>> response = offreController.getAllOffers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetOffreById_Success() {
        // Arrange
        OffreEmploi offre = new OffreEmploi();
        Entreprise entreprise = new Entreprise();
        entreprise.setNom("Test company");
        entreprise.setEmail("test@gmail.com");
        offre.setEntreprise(entreprise);
        when(offreEmploiService.getOffreById(1)).thenReturn(Optional.of(offre));

        // Act
        ResponseEntity<OffreDTO> response = offreController.getOffreById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetOffreById_NotFound() {
        // Arrange
        when(offreEmploiService.getOffreById(1)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<OffreDTO> response = offreController.getOffreById(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateOffre_Success() {
        // Arrange
        OffreDTO updatedOffreDTO = OffreDTO.builder()
                .title("Updated Title")
                .description("Updated Description")
                .build();

        OffreEmploi savedOffre = new OffreEmploi();
        Entreprise entreprise = new Entreprise();
        entreprise.setNom("Test company");
        entreprise.setEmail("test@gmail.com");
        savedOffre.setEntreprise(entreprise);
        when(offreEmploiService.updateOffre(eq(1), any(OffreEmploi.class))).thenReturn(savedOffre);

        // Act
        ResponseEntity<OffreDTO> response = offreController.updateOffre(1, updatedOffreDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testUpdateOffre_NotFound() {
        // Arrange
        OffreDTO updatedOffreDTO = new OffreDTO();
        when(offreEmploiService.updateOffre(eq(1), any(OffreEmploi.class)))
                .thenThrow(new RuntimeException("Offre not found"));

        // Act
        ResponseEntity<OffreDTO> response = offreController.updateOffre(1, updatedOffreDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteOffre_Success() {
        // Arrange
        doNothing().when(offreEmploiService).deleteOffre(1);

        // Act
        ResponseEntity<Void> response = offreController.deleteOffre(1);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(offreEmploiService).deleteOffre(1);
    }

    @Test
    void testDeleteOffre_NotFound() {
        // Arrange
        doThrow(new RuntimeException("Offre not found"))
                .when(offreEmploiService).deleteOffre(1);

        // Act
        ResponseEntity<Void> response = offreController.deleteOffre(1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSearchOffres_Success() {
        // Arrange
        OffreSearchRequestDTO searchDTO = new OffreSearchRequestDTO();
        searchDTO.setKeyword("Java");
        searchDTO.setTypeContrat(ContratType.CDI);
        searchDTO.setSalaryMin(40000.0);
        searchDTO.setSalaryMax(60000.0);
        searchDTO.setPage(0);
        searchDTO.setSize(10);
        searchDTO.setSortBy(SortBy.PUB_DATE);
        searchDTO.setSortDirection(SortDirection.DESC);
        EntrepriseDTO entrepriseDTO = new EntrepriseDTO();
        entrepriseDTO.setName("Test company");
        entrepriseDTO.setEmail("test@gmail.com");

        OffreSearchResponseDTO responseDTO = OffreSearchResponseDTO.builder()
                .title("Java Developer")
                .description("Job Description")
                .position("Senior Developer")
                .contractType(ContratType.CDI)
                .salary(50000.0)
                .publicationDate(LocalDateTime.now())
                .deadlineDate(LocalDateTime.now().plusDays(30))
                .timeAgo("2 days ago")
                .company(entrepriseDTO)
                .build();

        Page<OffreSearchResponseDTO> page = new PageImpl<>(List.of(responseDTO));
        when(offreEmploiService.searchOffres(any(OffreSearchRequestDTO.class))).thenReturn(page);

        // Act
        ResponseEntity<?> response = offreController.searchOffres(searchDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Page);
        Page<?> resultPage = (Page<?>) response.getBody();
        assertFalse(resultPage.getContent().isEmpty());
        verify(offreEmploiService).searchOffres(any(OffreSearchRequestDTO.class));
    }

    @Test
    void testSearchOffres_InternalServerError() {
        // Arrange
        OffreSearchRequestDTO searchDTO = new OffreSearchRequestDTO();
        when(offreEmploiService.searchOffres(any(OffreSearchRequestDTO.class)))
                .thenThrow(new RuntimeException("Internal error"));

        // Act
        ResponseEntity<?> response = offreController.searchOffres(searchDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}