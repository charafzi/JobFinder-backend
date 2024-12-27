package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Auth.*;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.exceptions.SamePasswordException;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.service.OtpService;
import com.ilisi.jobfinder.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email){
        try {
            String otp = otpService.generateAndStoreOtp(email); // Générer et stocker l'OTP
            otpService.sendOtpEmail(email, otp);
            otpService.SaveOtpforUser(email, otp);// Envoyer l'OTP par email
            return ResponseEntity.ok().body("Code OTP has been sent successfully.");
        }catch (EmailNotExist e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot send OTP code. No user registered with this email.");
        }
        catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during sending OTP code.");
        }
    }
    // Endpoint pour valider l'OTP
    @GetMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam String email, @RequestParam String otp)
    {
        String storedotp=otpService.getOtpForUser(email);
       return otp.equals(storedotp) ?
               ResponseEntity.ok().body("OTP valid") :
               ResponseEntity.badRequest().body("OTP Invalid");

    }
   @PostMapping("/resetPassword")
    public ResponseEntity<?> updatePassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
       try {
           User user = authService.updatePassword(resetPasswordRequest);
           return ResponseEntity.ok(user);
       } catch (EmailNotExist e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email does not exist.");
       } catch (SamePasswordException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The new password cannot be the same as the old password.");
       }
       catch (Exception e){
           return ResponseEntity.badRequest().body(null);
       }
   }
}
