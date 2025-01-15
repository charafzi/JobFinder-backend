package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.Auth.RegisterCandidatRequest;
import com.ilisi.jobfinder.dto.DocumentDTO;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.CandidatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CandidatService {
    private final UserService userService;
    private final CandidatRepository candidatRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentService documentService;
    private final String uploadDir = "src/main/java/com/ilisi/jobfinder/assets/candidatProfilePic";
    private final Path storageLocation = Paths.get(System.getProperty("user.dir"), uploadDir);

    public User registerCandidat(RegisterCandidatRequest candidatRequest) throws EmailAlreadyExists {
        if (userService.getUserByEmail(candidatRequest.getEmail()).isPresent()) {
           throw new EmailAlreadyExists("Email already exists");
        }
        Candidat candidat = new Candidat();
        candidat.setEmail(candidatRequest.getEmail());
        candidat.setPassword(passwordEncoder.encode(candidatRequest.getPassword())); // Hashage du mot de passe
        candidat.setNom(candidatRequest.getLastName());
        candidat.setPrenom(candidatRequest.getFirstName());
        candidat.setTelephone(candidatRequest.getPhoneNumber());
        candidat.setRole(Role.CANDIDAT);

        return userService.saveUser(candidat);
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

    public byte[] getProfilePictureData(String email) throws IOException {
        User user = userService.getUserByEmail(email)
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

    public void uploadDocument(String email, MultipartFile file, DocumentType type) throws IOException, EntityNotFoundException, RuntimeException{
        Optional<User> user = userService.getUserByEmail(email);

        if(user.isEmpty()){
            throw new EntityNotFoundException("Candidat with email = "+email+" not found");
        }
        Candidat candidat = (Candidat) user.get();

        try {
            this.documentService.saveDocument(candidat, file, type);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de sauvegardement du ficher.");
        }
        catch (RuntimeException e){
            throw e;
        }
    }

    public DocumentDTO getDocumentById(Long id) throws IOException {
        return this.documentService.getDocumentById(id);
    }
}

