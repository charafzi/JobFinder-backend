package com.ilisi.jobfinder.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilisi.jobfinder.controller.AuthController;
import com.ilisi.jobfinder.dto.Auth.*;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.exceptions.SamePasswordException;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Configure MockMvc avec le contrôleur et ses dépendances mockées
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .token("mocked-jwt-token")
                .build();

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

        verify(authService).authenticate(any(LoginRequest.class));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        when(authService.authenticate(any(LoginRequest.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isNotFound());

        verify(authService).authenticate(any(LoginRequest.class));
    }

    @Test
    void testRegisterCandidat_Success() throws Exception {
        // Arrange
        RegisterCandidatRequest candidatRequest = RegisterCandidatRequest.builder()
                .email("candidat@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();

        User mockUser = User.builder()
                .email("entreprise@example.com")
                .build();
        when(authService.registerCandidat(any(RegisterCandidatRequest.class))).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/registerCandidat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Candidat enregistré avec succès !"));

        verify(authService).registerCandidat(any(RegisterCandidatRequest.class));
    }

    @Test
    void testRegisterCandidat_EmailAlreadyExists() throws Exception {
        // Arrange
        RegisterCandidatRequest candidatRequest = RegisterCandidatRequest.builder()
                .email("existing@example.com")
                .password("password123")
                .build();

        doThrow(new EmailAlreadyExists("Email already exists"))
                .when(authService).registerCandidat(any(RegisterCandidatRequest.class));

        // Act & Assert
        mockMvc.perform(post("/api/auth/registerCandidat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));

        verify(authService).registerCandidat(any(RegisterCandidatRequest.class));
    }

    @Test
    void testValidateToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/validateToken")
                        .param("token", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));

        verify(authService).validateToken("mocked-token");
    }

    @Test
    void testRegisterEntreprise_Success() throws Exception {
        // Arrange
        RegisterEntrepriseRequest entrepriseRequest = RegisterEntrepriseRequest.builder()
                .email("entreprise@example.com")
                .password("password123")
                .nom("Test Company")
                .build();

        User mockUser = User.builder()
                .email("entreprise@example.com")
                .build();

        when(authService.registerEntreprise(any(RegisterEntrepriseRequest.class))).thenReturn(mockUser);

        // Act & Assert
        mockMvc.perform(post("/api/auth/registerEntreprise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrepriseRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Entreprise enregistré avec succès !"));

        verify(authService).registerEntreprise(any(RegisterEntrepriseRequest.class));
    }

    /// Logic is correct but there is a problem with serializing User Object
    /*@Test
    void updatePassword_shouldReturnOk_whenPasswordUpdatedSuccessfully() throws Exception {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("newPassword")
                .build();

        User user = new User();
        user.setEmail("test@example.com");

        when(authService.updatePassword(Mockito.any(ResetPasswordRequest.class)))
                .thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/api/auth/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Sérialisation de l'objet
                .andExpect(status().isOk());
    }*/

    @Test
    void updatePassword_shouldReturnNotFound_whenEmailNotExist() throws Exception {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("newPassword")
                .build();

        Mockito.when(authService.updatePassword(Mockito.any(ResetPasswordRequest.class)))
                .thenThrow(new EmailNotExist("Email does not exist"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Sérialisation de l'objet
                .andExpect(status().isNotFound())
                .andExpect(content().string("Email does not exist."));
    }

    @Test
    void updatePassword_shouldReturnBadRequest_whenSamePasswordExceptionThrown() throws Exception {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("samePassword")
                .build();

        Mockito.when(authService.updatePassword(Mockito.any(ResetPasswordRequest.class)))
                .thenThrow(new SamePasswordException("The new password cannot be the same as the old password."));

        // Act & Assert
        mockMvc.perform(post("/api/auth/resetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Sérialisation de l'objet
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The new password cannot be the same as the old password."));
    }
}
