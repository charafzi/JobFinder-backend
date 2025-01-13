package com.ilisi.jobfinder.controller;


import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.service.CandidatureService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidature")
@AllArgsConstructor
public class CandidatureController {
    private final CandidatureService candidatureService;

    @PostMapping(value = "/postuler", consumes = "multipart/form-data")
    public ResponseEntity<String> postulerCandidature(@ModelAttribute @Valid CandidatureRequest request) {
        try {
            candidatureService.postuler(request);
            return ResponseEntity.ok("Candidature soumise avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }
}
