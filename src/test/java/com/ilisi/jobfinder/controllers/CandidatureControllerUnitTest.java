package com.ilisi.jobfinder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.controller.CandidatureController;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureStatusUpdateResquest;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.service.CandidatureService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CandidatureControllerUnitTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CandidatureController candidatureController;

    @Mock
    private CandidatureService candidatureService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(candidatureController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void postulerCandidature_Success() throws Exception {
        // Arrange
        CandidatureRequest request = new CandidatureRequest();
        MockMultipartFile file = new MockMultipartFile(
            "cv",
            "cv.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "test cv content".getBytes()
        );

        doNothing().when(candidatureService).postuler(any(CandidatureRequest.class));

        // Act & Assert
        mockMvc.perform(multipart("/api/candidature/postuler")
                .file(file)
                .param("candidatId", "1")
                .param("offreId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Candidature soumise avec succès !"));
    }

    @Test
    void postulerCandidature_EntityNotFound() throws Exception {
        // Arrange
        CandidatureRequest request = new CandidatureRequest();
        MockMultipartFile file = new MockMultipartFile(
            "cv",
            "cv.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "test cv content".getBytes()
        );

        doThrow(new EntityNotFoundException("Entity not found")).when(candidatureService).postuler(any(CandidatureRequest.class));

        // Act & Assert
        mockMvc.perform(multipart("/api/candidature/postuler")
                .file(file)
                .param("candidatId", "1")
                .param("offreId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Erreur : Entity not found"));
    }

    @Test
    void acceptCandidature_Success() throws Exception {
        // Arrange
        CandidatureStatusUpdateResquest request = new CandidatureStatusUpdateResquest();
        request.setEmail("test@email.com");
        request.setOffreId(1L);

        doNothing().when(candidatureService).changeCandidatureStatus(any(), eq(CandidatureStatus.ACCEPTE));

        // Act & Assert
        mockMvc.perform(put("/api/candidature/accept")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Candidature acceptée avec succées"));
    }

    @Test
    void dismissCandidature_Success() throws Exception {
        // Arrange
        CandidatureStatusUpdateResquest request = new CandidatureStatusUpdateResquest();
        request.setEmail("test@email.com");
        request.setOffreId(1L);

        doNothing().when(candidatureService).changeCandidatureStatus(any(), eq(CandidatureStatus.REJETEE));

        // Act & Assert
        mockMvc.perform(put("/api/candidature/dismiss")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Candidature rejetée avec succées"));
    }

    @Test
    void checkIfUserApplied_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Long offreId = 1L;
        when(candidatureService.checkIfUserApplied(userId, offreId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/candidature/check/{userId}/{offreId}", userId, offreId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getAllCandidaturesByUser_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        int page = 0;
        int size = 10;

        when(candidatureService.getAllCandidaturesByUser(eq(userId), eq(page), eq(size)))
            .thenReturn(new PageResponse<>());

        // Act & Assert
        mockMvc.perform(get("/api/candidature/")
                .param("id", userId.toString())
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }
}
