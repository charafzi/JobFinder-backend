package com.ilisi.jobfinder.services.unitTest;

import com.ilisi.jobfinder.dto.Auth.LoginRequest;
import com.ilisi.jobfinder.dto.Auth.LoginResponse;
import com.ilisi.jobfinder.dto.Auth.RegisterCandidatRequest;
import com.ilisi.jobfinder.dto.Auth.ResetPasswordRequest;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.exceptions.SamePasswordException;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private CandidatService candidatService;
    @Mock
    private EntrepriseService entrepriseService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder, jwtService, authenticationManager, userService, candidatService, entrepriseService);
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user.getEmail())).thenReturn("mocked-jwt-token");

        // Act
        LoginResponse response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(user.getEmail());
    }

    @Test
    void testAuthenticate_Failure() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(loginRequest));
    }

    @Test
    void testRegisterCandidat_Success() throws EmailAlreadyExists {
        // Arrange
        RegisterCandidatRequest request = RegisterCandidatRequest.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();

        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        when(candidatService.registerCandidat(request)).thenReturn(mockUser);

        // Act
        User registeredUser = authService.registerCandidat(request);

        // Assert
        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(candidatService).registerCandidat(request);
    }

    @Test
    void testRegisterCandidat_EmailAlreadyExists() throws EmailAlreadyExists {
        // Arrange
        RegisterCandidatRequest request = RegisterCandidatRequest.builder().email("existing@example.com").build();

        when(candidatService.registerCandidat(request)).thenThrow(new EmailAlreadyExists("Email already exists"));

        // Act & Assert
        assertThrows(EmailAlreadyExists.class, () -> authService.registerCandidat(request));
    }

    @Test
    void testVerifyUser_Success() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        // Act
        boolean isValid = authService.verifyUser(loginRequest);

        // Assert
        assertTrue(isValid);
        verify(userService).getUserByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
    }

    @Test
    void testVerifyUser_Failure() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Act
        boolean isValid = authService.verifyUser(loginRequest);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void updatePassword_shouldUpdatePassword_whenUserExistsAndNewPasswordIsDifferent() {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("newPassword")
                .build();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("oldEncodedPassword");

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("newPassword", "oldEncodedPassword"))
                .thenReturn(false);

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("newEncodedPassword");

        // Act
        User updatedUser = authService.updatePassword(request);

        // Assert
        assertEquals("newEncodedPassword", updatedUser.getPassword());
        verify(userService).saveUser(updatedUser);
    }

    @Test
    void updatePassword_shouldThrowEmailNotExist_whenUserDoesNotExist() {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("newPassword")
                .build();

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotExist.class, () -> authService.updatePassword(request));
    }

    @Test
    void updatePassword_shouldThrowSamePasswordException_whenOldPasswordMatchesNewPassword() {
        // Arrange
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .email("test@example.com")
                .newPassword("samePassword")
                .build();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("samePassword", "encodedPassword"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(SamePasswordException.class, () -> authService.updatePassword(request));
    }


}
