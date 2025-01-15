package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.exceptions.EmailNotExist;
import com.ilisi.jobfinder.model.SecteurActivite;
import com.ilisi.jobfinder.service.EntrepriseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/entreprise")
@AllArgsConstructor
public class EntrepriseController {
    private final EntrepriseService entrepriseService;

    @PutMapping()
    public ResponseEntity<EntrepriseDTO> updateEntreprise(@RequestBody EntrepriseDTO entrepriseDTO){
        try {
            EntrepriseDTO e = this.entrepriseService.updateEntreprise(entrepriseDTO);
            return ResponseEntity.ok(e);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteEntrepriseByEmail(@PathVariable String email){
        try {
            this.entrepriseService.deleteEntrepriseByEmail(email);
            return ResponseEntity.ok("Entreprise with email="+email+" deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<EntrepriseDTO> getEntrepriseByEmail(@PathVariable String email){
        try {
            EntrepriseDTO entrepriseDTO = this.entrepriseService.getEntrepriseByEmail(email);
            return ResponseEntity.ok(entrepriseDTO);
        } catch (EmailNotExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @GetMapping("/secteurs-activites")
    public List<SecteurActivite> getSecteurActivites(){
       return this.entrepriseService.getSecteursActivites();
    }

    @PostMapping("/profile-picture/{id}")
    public ResponseEntity<String> saveUserPicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file){


        if (file.isEmpty()) {
            System.out.println("File is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            System.out.println("Invalid file type: " + file.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type: " + file.getContentType());
        }

        try {
            String imageUrl = entrepriseService.uploadProfilePicture(id,file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long id) {
        try {
            // Get the binary data of the image
            byte[] imageData = entrepriseService.getProfilePictureData(id);

            // Check if the image data exists
            if (imageData != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
