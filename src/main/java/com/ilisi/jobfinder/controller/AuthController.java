package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.*;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.service.OtpService;
import com.ilisi.jobfinder.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private OtpService otpService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Tentative de connexion avec : " + loginRequest.getEmail());
        try{
            LoginResponse loginResponse =authService.authenticate(loginRequest);
            //log.info("Authentification user :{} "+loginRequest.getEmail());
            return ResponseEntity.ok(loginResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(null); //invalid email
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null); //invalid password
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(null); //generic authentication failure
        }
    }
    @GetMapping("/validateToken")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }

    @PostMapping("/registerCandidat")
    public ResponseEntity<?> registerCandidat(@RequestBody RegisterCandidatRequest candidatRequest) {
        try {
            authService.registerCandidat(candidatRequest);
        } catch (EmailAlreadyExists e) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        return ResponseEntity.ok("Candidat enregistré avec succès !");
    }
   @PostMapping("/registerEntreprise")
   public ResponseEntity<?> registerEntreprise(@RequestBody RegisterEntrepriseRequest entrepriseRequest) {
       try {
           authService.registerEntreprise(entrepriseRequest);
       } catch (EmailAlreadyExists e) {
           return ResponseEntity.badRequest().body("Email already exists");
       }
       return ResponseEntity.ok("Entreprise enregistré avec succès !");
   }

    @PostMapping("/send-otp")
    public void sendOtp(@RequestParam String email) throws MessagingException {
        String otp = otpService.generateAndStoreOtp(email); // Générer et stocker l'OTP
        otpService.sendOtpEmail(email, otp);
        otpService.SaveOtpforUser(email, otp);// Envoyer l'OTP par email
    }
    // Endpoint pour valider l'OTP
    @PostMapping("/validate-otp")
    public boolean validateOtp(@RequestParam String email, @RequestParam String otp)
    {
        String storedotp=otpService.getOtpForUser(email);
        return otp.equals(storedotp);
    }
//    @PostMapping("/authenticateWithGoogle")
//    public ResponseEntity<?> authenticateWithGoogle(@RequestBody @Valid Auth0Request auth0Request) {
//        try {
//            UserDTO userDTO = authService.authenticateWithGoogle(auth0Request);
//            return ResponseEntity.ok(userDTO);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erreur lors de l'authentification avec Google : " + e.getMessage());
//        }
//    }




}
