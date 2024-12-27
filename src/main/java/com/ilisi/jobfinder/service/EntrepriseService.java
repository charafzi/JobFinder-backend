package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.Auth.RegisterEntrepriseRequest;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.mapper.AdresseMapper;
import com.ilisi.jobfinder.mapper.EntrepriseMapper;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        entreprise.setNom(entrepriseRequest.getNom());
        entreprise.setPassword(passwordEncoder.encode(entrepriseRequest.getPassword())); // Hashage du mot de passe
        entreprise.setAdresse(AdresseMapper.toEntity(entrepriseRequest.getAdresse()));
        entreprise.setTelephone(entrepriseRequest.getPhoneNumber());
        entreprise.setRole(Role.ENTREPRISE);

        return userService.saveUser(entreprise);
    }

    public EntrepriseDTO updateEntreprise(EntrepriseDTO entrepriseDTO){
        User user = this.userService.getUserById(entrepriseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entreprise with id=" + entrepriseDTO.getId() + " not found."));

        if (!(user instanceof Entreprise)) {
            throw new IllegalStateException("User with id=" + entrepriseDTO.getId() + " is not an Entreprise.");
        }

        // Cast User to Entreprise
        Entreprise entreprise = (Entreprise) user;

        if (entrepriseDTO.getNom() != null) {
            entreprise.setNom(entrepriseDTO.getNom());
        }
        if (entrepriseDTO.getPhotoProfile() != null) {
            entreprise.setPhotoProfile(entrepriseDTO.getPhotoProfile());
        }
        if (entrepriseDTO.getTelephone() != null) {
            entreprise.setTelephone(entrepriseDTO.getTelephone());
        }
        if (entrepriseDTO.getAbout() != null) {
            entreprise.setAbout(entrepriseDTO.getAbout());
        }
        if (entrepriseDTO.getAdresse() != null) {
            entreprise.setAdresse(AdresseMapper.toEntity(entrepriseDTO.getAdresse()));
        }
        // Save the updated Entreprise entity
        entreprise = (Entreprise) this.userService.saveUser(entreprise);

        // Return the updated DTO
        return EntrepriseMapper.toDto(entreprise);

    }

    public void deleteEntrepriseByEmail(String email){
        this.userService.deleteUserByEmail(email);
    }
}
