package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.DocumentDTO;
import com.ilisi.jobfinder.service.CandidatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/candidat")
@AllArgsConstructor
public class CandidatController {
    private final CandidatService candidatService;
    @PostMapping("/profile-picture/{email}")
    public ResponseEntity<String> saveUserPicture(
            @PathVariable String email,
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
            String imageUrl = candidatService.uploadProfilePicture(email,file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/profile-picture/{email}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String email) {
        try {
            // Get the binary data of the image
            byte[] imageData = candidatService.getProfilePictureData(email);

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

    @PostMapping("/document")
    public ResponseEntity<?> uploadDocumentByUser(
            @NonNull @RequestParam("email") String email,
            @NonNull @RequestParam("type") DocumentType type,
            @NonNull @RequestParam("document") MultipartFile document
            ){
        try {
            this.candidatService.uploadDocument(email,document,type);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/document/{docId}")
    public ResponseEntity<byte[]> getDocumentById(@PathVariable Long docId) {
        try {
            DocumentDTO documentDTO = candidatService.getDocumentById(docId);

            // Check if the image data exists
            if (documentDTO.getData()!= null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(documentDTO.getContentType());
                return new ResponseEntity<>(documentDTO.getData(), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (IOException e ){
            return ResponseEntity.internalServerError().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
