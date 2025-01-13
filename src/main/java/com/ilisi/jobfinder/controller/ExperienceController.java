package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Experience.ExperienceRequest;
import com.ilisi.jobfinder.dto.Experience.ExperienceResponse;
import com.ilisi.jobfinder.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/experience")
public class ExperienceController {
    private final ExperienceService experienceService;

    @PostMapping
    public ResponseEntity<Void> createExperience(@RequestBody @Valid ExperienceRequest request){
        try{
            experienceService.createExperience(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (RuntimeException e){return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<ExperienceResponse>> getExperiencesByCandidat(@PathVariable Long candidatId) {
        List<ExperienceResponse> exprience = experienceService.getExprienceByCandidat(candidatId);
        return ResponseEntity.ok(exprience); // 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExperience(@PathVariable Long id, @RequestBody @Valid ExperienceRequest request) {
        try {
            experienceService.updateExperience(id, request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    // Supprimer une formation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        try {
            experienceService.deleteExperience(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    // Récupérer une formation par ID
    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponse> getExperienceById(@PathVariable Long id) {
        try {
            ExperienceResponse experienceResponse = experienceService.getExperienceById(id);
            return ResponseEntity.ok(experienceResponse); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }




}
