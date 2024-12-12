package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.CandidatRequest;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CandidatService {
    private final UserService userService;
    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public CandidatService(UserService userService) {
        this.userService = userService;
    }
    public User registerCandidat(CandidatRequest candidatRequest) {
        if (userService.getUserByEmail(candidatRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
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

