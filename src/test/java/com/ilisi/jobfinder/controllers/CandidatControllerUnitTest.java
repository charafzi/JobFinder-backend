package com.ilisi.jobfinder.controllers;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.controller.CandidatController;
import com.ilisi.jobfinder.service.CandidatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CandidatControllerUnitTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CandidatController candidatController;

    @Mock
    private CandidatService candidatService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(candidatController).build();
    }

    @Test
    void saveUserPicture_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );
        
        when(candidatService.uploadProfilePicture(eq(userId), any())).thenReturn("http://example.com/image.jpg");

        // Act & Assert
        mockMvc.perform(multipart("/api/candidat/profile-picture/{id}", userId)
                .file(file))
                .andExpect(status().isOk());
    }

    @Test
    void saveUserPicture_EmptyFile() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/candidat/profile-picture/{id}", userId)
                .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserPicture_InvalidFileType() throws Exception {
        // Arrange
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "test content".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/candidat/profile-picture/{id}", userId)
                .file(file))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadDocumentByUser_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        DocumentType type = DocumentType.CV;
        MockMultipartFile document = new MockMultipartFile(
            "document",
            "cv.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "test cv content".getBytes()
        );

        doNothing().when(candidatService).uploadDocument(eq(email), any(), eq(type));

        // Act & Assert
        mockMvc.perform(multipart("/api/candidat/document")
                .file(document)
                .param("email", email)
                .param("type", type.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocumentByUser_EmptyFile() throws Exception {
        // Arrange
        String email = "test@example.com";
        DocumentType type = DocumentType.CV;
        MockMultipartFile document = new MockMultipartFile(
            "document",
            "cv.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            new byte[0]
        );

        doThrow(new IOException("Empty file")).when(candidatService).uploadDocument(eq(email), any(), eq(type));

        // Act & Assert
        mockMvc.perform(multipart("/api/candidat/document")
                .file(document)
                .param("email", email)
                .param("type", type.toString()))
                .andExpect(status().isBadRequest());
    }
}
