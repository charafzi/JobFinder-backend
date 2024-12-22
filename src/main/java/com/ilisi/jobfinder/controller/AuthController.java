package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.*;
//import com.ilisi.jobfinder.service.OtpService;
import com.ilisi.jobfinder.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//    private OtpService otpService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest){
        System.out.println("Tentative de connexion avec : " + loginRequest.getEmail());
        try{
            UserDTO user=authService.authenticate(loginRequest);
            log.info("Authentification user :{} "+loginRequest.getEmail());
            return ResponseEntity.ok(user);
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
    public ResponseEntity<?> registerCandidat(@RequestBody CandidatRequest candidatRequest) {
        authService.registerCandidat(candidatRequest);
        return ResponseEntity.ok("Candidat enregistré avec succès !");
    }
   @PostMapping("/registerEntreprise")
   public ResponseEntity<?> registerEntreprise(@RequestBody EntrepriseRequest entrepriseRequest) {
       authService.registerEntreprise(entrepriseRequest);
       return ResponseEntity.ok("Entreprise enregistré avec succès !");
   }

//    @PostMapping("/send-otp")
//    public void sendOtp(@RequestParam String email) throws MessagingException {
//        String otp = otpService.generateAndStoreOtp(email); // Générer et stocker l'OTP
//        otpService.sendOtpEmail(email, otp);
//        otpService.SaveOtpforUser(email, otp);// Envoyer l'OTP par email
//    }
//    // Endpoint pour valider l'OTP
//    @PostMapping("/validate-otp")
//    public boolean validateOtp(@RequestParam String email, @RequestParam String otp)
//    {
//        String storedotp=otpService.getOtpForUser(email);
//        return otp.equals(storedotp);
//    }
//    @PostMapping("/authenticateWithGoogle")
//    public ResponseEntity<UserDTO> authenticateWithGoogle(@RequestBody Auth0Request auth0Request) {
//        return ResponseEntity.ok(authService.authenticateWithGoogle(auth0Request));
//    }



}
