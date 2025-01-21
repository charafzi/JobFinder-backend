package com.ilisi.jobfinder.service;


import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.Auth.RegisterEntrepriseRequest;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.mapper.AdresseMapper;
import com.ilisi.jobfinder.mapper.EntrepriseMapper;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.SecteurActivite;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.EntrepriseRepository;
import com.ilisi.jobfinder.repository.SecteurActiviteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class EntrepriseService {
    private final UserService userService;
    private final SecteurActiviteRepository secteurActiviteRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntrepriseRepository entrepriseRepository;
    private final String uploadDir = "src/main/java/com/ilisi/jobfinder/assets/entrepriseProfilePic";
    private final Path storageLocation = Paths.get(System.getProperty("user.dir"), uploadDir);

    public User registerEntreprise(RegisterEntrepriseRequest entrepriseRequest) throws EmailAlreadyExists{
        if (userService.getUserByEmail(entrepriseRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExists("Email already exists");
        }
        Entreprise entreprise = new Entreprise();
        entreprise.setEmail(entrepriseRequest.getEmail());
        entreprise.setNom(entrepriseRequest.getNom());
        entreprise.setAbout(entrepriseRequest.getAbout());
        entreprise.setPassword(passwordEncoder.encode(entrepriseRequest.getPassword())); // Hashage du mot de passe
        entreprise.setAdresse(AdresseMapper.toEntity(entrepriseRequest.getAdress()));
        entreprise.setTelephone(entrepriseRequest.getPhoneNumber());
        entreprise.setRole(Role.ENTREPRISE);

        return userService.saveUser(entreprise);
    }

    public EntrepriseDTO getEntrepriseByEmail(String email) throws EmailNotExist{
        Optional<User> oUser = userService.getUserByEmail(email);
        if (oUser.isEmpty()) {
            throw new EmailNotExist("Entreprise with email=" +  email + " is not existing.");
        }
        User user = this.userService.getUserByEmail(email).get();

        if (!(user instanceof Entreprise)) {
            throw new IllegalStateException("User with email=" +  email + " is not an Entreprise.");
        }

        Entreprise entreprise = (Entreprise) user;

        return EntrepriseMapper.toDto(entreprise);
    }

    public EntrepriseDTO updateEntreprise(EntrepriseDTO entrepriseDTO){
        User user = this.userService.getUserById(entrepriseDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Entreprise with id=" + entrepriseDTO.getId() + " not found."));

        if (!(user instanceof Entreprise)) {
            throw new IllegalStateException("User with id=" + entrepriseDTO.getId() + " is not an Entreprise.");
        }

        // Cast User to Entreprise
        Entreprise entreprise = (Entreprise) user;

        if (entrepriseDTO.getName() != null) {
            entreprise.setNom(entrepriseDTO.getName());
        }
        if (entrepriseDTO.getProfilePicture() != null) {
            entreprise.setPhotoProfile(entrepriseDTO.getProfilePicture());
        }
        if (entrepriseDTO.getPhoneNumber() != null) {
            entreprise.setTelephone(entrepriseDTO.getPhoneNumber());
        }
        if (entrepriseDTO.getAbout() != null) {
            entreprise.setAbout(entrepriseDTO.getAbout());
        }
        if (entrepriseDTO.getAdress() != null) {
            entreprise.setAdresse(AdresseMapper.toEntity(entrepriseDTO.getAdress()));
        }
        if(entrepriseDTO.getAbout() != null){
            entreprise.setSecteurActivites(entrepriseDTO.getActivitySectors());
        }
        if(entrepriseDTO.getAbout() != null){
            entreprise.setAbout(entrepriseDTO.getAbout());
        }
        // Save the updated Entreprise entity
        entreprise = (Entreprise) this.userService.saveUser(entreprise);

        // Return the updated DTO
        return EntrepriseMapper.toDto(entreprise);

    }

    public void deleteEntrepriseByEmail(String email){
        this.userService.deleteUserByEmail(email);
    }

    public String uploadProfilePicture(Long id, MultipartFile file) throws IOException {
        // Retrieve the user from the database
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //ensure directory exists
        Files.createDirectories(storageLocation);

        try (Stream<Path> files = Files.list(storageLocation)) {
            files
                    .filter(path -> path.getFileName().toString().startsWith("profile_" + id+ "_"))
                    .forEach(existingFile -> {
                        try {
                            Files.delete(existingFile);
                        } catch (IOException e) {
                            System.err.println("Failed to delete old profile picture: " + existingFile);
                        }
                    });
        }

        //generate user profile url
        String originalFilename = file.getOriginalFilename();
        // Extract the file extension
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        LocalDateTime now = LocalDateTime.now();
        // Format the timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = now.format(formatter);
        // Construct the user profile URL with only the file extension
        String userProfileUrl = "profile_" + id + "_" + timestamp + fileExtension;
        Path filePath = storageLocation.resolve(userProfileUrl);

        //save file on server locally
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update the user's profile picture URL in the database
        user.setPhotoProfile(userProfileUrl);
        userService.saveUser(user);
        return userProfileUrl;
    }


    public byte[] getProfilePictureData(Long id) throws IOException {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the URL or file path of the stored image
        String imageUrl = user.getPhotoProfile();
        System.out.println("image url : "+imageUrl);
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        Path imagePath = storageLocation.resolve(imageUrl);

        if (!Files.exists(imagePath)) {
            return null;
        }

        return Files.readAllBytes(imagePath);
    }

    public List<SecteurActivite> getSecteursActivites() {
        return this.secteurActiviteRepository.findAll().stream().toList();
    }
    public EntrepriseDTO updateEntrepriseSecteurs(Long entrepriseId, List<Long> secteurIds) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> new RuntimeException("Entreprise not found"));

        List<SecteurActivite> secteurs = secteurActiviteRepository.findAllById(secteurIds);
        entreprise.setSecteurActivites(secteurs);

        Entreprise updatedEntreprise = entrepriseRepository.save(entreprise);
        return EntrepriseMapper.toDto(updatedEntreprise);
    }


}
