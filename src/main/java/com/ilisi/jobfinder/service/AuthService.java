package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.controller.AuthController;
import com.ilisi.jobfinder.dto.Auth.*;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.exceptions.SamePasswordException;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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


    public LoginResponse authenticate(LoginRequest loginRequest) throws UsernameNotFoundException, BadCredentialsException {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String jwtToken = jwtService.generateToken(user.getEmail());
        return LoginResponse.builder().token(jwtToken).build();
    }


    public User registerCandidat(RegisterCandidatRequest candidatRequest) throws EmailAlreadyExists {
        return candidatService.registerCandidat(candidatRequest);
    }
    public User registerEntreprise(RegisterEntrepriseRequest entrepriseRequest) throws EmailAlreadyExists{
        return entrepriseService.registerEntreprise(entrepriseRequest);
    }


//    public UserDTO authenticateWithGoogle(Auth0Request auth0Request) {
//        Optional<User> userOpt;
//        try {
//            // Recherche de l'utilisateur existant par Google ID
//            userOpt = userService.getUserbyGoogleId(auth0Request.getSub());
//        } catch (Exception e) {
//            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
//            throw e;
//        }
//
//        User user;
//        if (userOpt.isPresent()) {
//            // Si l'utilisateur existe déjà
//            user = userOpt.get();
//        } else {
//            // Si l'utilisateur n'existe pas, en créer un nouveau
//            user = auth0Request.toUser();  // Utilisation de la méthode toUser() pour créer l'utilisateur
//
//            // Sauvegarder l'utilisateur
//            user = userService.createUser(user);
//        }
//
//        System.out.println("Utilisateur authentifié ou créé : " + user);
//
//        // Génération du token JWT
//        String token = jwtService.generateToken(user.getEmail());
//
//        // Construire et retourner le UserDTO
//        return UserDTO.builder()
//                .id(user.getId())
//                .email(user.getEmail())
//                .token(token)
//                .role(user.getRole())
//                .build();
//    }



}
