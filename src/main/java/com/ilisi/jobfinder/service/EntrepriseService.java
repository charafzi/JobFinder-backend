package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.EntrepriseRequest;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EntrepriseService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerEntreprise(EntrepriseRequest entrepriseRequest) {
        if (userService.getUserByEmail(entrepriseRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        Entreprise entreprise = new Entreprise();
        entreprise.setEmail(entrepriseRequest.getEmail());
        entreprise.setPassword(passwordEncoder.encode(entrepriseRequest.getPassword())); // Hashage du mot de passe
        entreprise.setAdresse(entrepriseRequest.getAdresse());
        entreprise.setTelephone(entrepriseRequest.getPhoneNumber());
        entreprise.setRole(Role.ENTREPRISE);

        return userService.createUser(entreprise);
    }
}
