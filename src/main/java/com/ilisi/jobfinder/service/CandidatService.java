package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.RegisterCandidatRequest;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CandidatService {
    private final UserService userService;
    public PasswordEncoder passwordEncoder;

    public User registerCandidat(RegisterCandidatRequest candidatRequest) throws EmailAlreadyExists {
        if (userService.getUserByEmail(candidatRequest.getEmail()).isPresent()) {
           throw new EmailAlreadyExists("Email already exists");
        }
        Candidat candidat = new Candidat();
        candidat.setEmail(candidatRequest.getEmail());
        candidat.setPassword(passwordEncoder.encode(candidatRequest.getPassword())); // Hashage du mot de passe
        candidat.setPhotoProfile(candidatRequest.getPhotoProfile());
        candidat.setNom(candidatRequest.getFirstName());
        candidat.setPrenom(candidatRequest.getLastName());
        candidat.setTelephone(candidatRequest.getPhoneNumber());
        candidat.setRole(Role.CANDIDAT);

        return userService.createUser(candidat);
    }
}

