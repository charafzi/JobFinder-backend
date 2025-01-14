package com.ilisi.jobfinder.controller;


import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.CandidatureDTO;
import com.ilisi.jobfinder.exceptions.OffreDejaPostule;
import com.ilisi.jobfinder.service.CandidatureService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;

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
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
        catch (OffreDejaPostule e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());

        }
    }

    @GetMapping("/{offreId}")
    public ResponseEntity<List<CandidatureDTO>> getAllCandidaturesByOffre(@PathVariable Long offreId) {
        List<CandidatureDTO> candidatureDTOS = this.candidatureService.getAllCandidaturesByOffre(offreId);
        return ResponseEntity.ok().body(candidatureDTOS);
    }
}
