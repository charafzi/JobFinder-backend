package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.RegisterEntrepriseRequest;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EntrepriseService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public User registerEntreprise(RegisterEntrepriseRequest entrepriseRequest) throws EmailAlreadyExists{
        if (userService.getUserByEmail(entrepriseRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExists("Email already exists");
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
