package com.ilisi.jobfinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilisi.jobfinder.controller.AuthController;
import com.ilisi.jobfinder.dto.Auth.*;
import com.ilisi.jobfinder.dto.CandidatDTO;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.exceptions.SamePasswordException;
import com.ilisi.jobfinder.mapper.CandidatMapper;
import com.ilisi.jobfinder.mapper.EntrepriseMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.security.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CandidatService candidatService;
    private final EntrepriseService entrepriseService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    public void validateToken(String token) {
        try {
            String email = jwtService.extractEmail(token);
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                jwtService.validateToken(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean emailExists(String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.isPresent();
    }
    
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.saveUser(user);
    }
    public ResponseEntity<Iterable<User>> getAllUsers(){
      return (ResponseEntity<Iterable<User>>) userService.getAllUsers();
    }

    public User updatePassword(ResetPasswordRequest resetPasswordRequest) throws EmailNotExist, SamePasswordException {
        Optional<User> userOptional = userService.getUserByEmail(resetPasswordRequest.getEmail());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(passwordEncoder.matches(resetPasswordRequest.getNewPassword(),user.getPassword()))
                throw new SamePasswordException("Old password matches new password");
            else{
                user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
                userService.saveUser(user);
            }
            return user;
        }else
            throw new EmailNotExist("No user with email = "+resetPasswordRequest.getEmail());
    }
    public boolean verifyUser(LoginRequest loginRequest) {
        User user = userService.getUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
        // Vérification du mot de passe
        return passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
    }


    public Object authenticate(LoginRequest loginRequest) throws UsernameNotFoundException, BadCredentialsException {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String jwtToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        if(user instanceof Entreprise){
            EntrepriseDTO entrepriseDTO = EntrepriseMapper.toDto((Entreprise) user);
            entrepriseDTO.setToken(jwtToken);
            entrepriseDTO.setRefreshToken(refreshToken);
            return entrepriseDTO;
        }else if (user instanceof Candidat){
            CandidatDTO candidatDTO = CandidatMapper.toDto((Candidat) user);
            candidatDTO.setToken(jwtToken);
            candidatDTO.setRefreshToken(refreshToken);
            return candidatDTO;
        }

        throw new IllegalStateException("Unknown user type");
    }


    public User registerCandidat(RegisterCandidatRequest candidatRequest) throws EmailAlreadyExists {
        return candidatService.registerCandidat(candidatRequest);
    }
    public User registerEntreprise(RegisterEntrepriseRequest entrepriseRequest) throws EmailAlreadyExists{
        return entrepriseService.registerEntreprise(entrepriseRequest);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractEmail(refreshToken);

        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user.getEmail());
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
