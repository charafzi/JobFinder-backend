package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Formation.FormationRequest;
import com.ilisi.jobfinder.dto.Formation.FormationResponse;
import com.ilisi.jobfinder.service.FormationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/formation")
public class FormationController {
    private final FormationService formationService;

    @PostMapping
    public ResponseEntity<Void> createFormation(@RequestBody @Valid FormationRequest request) {
        try {
            formationService.createFormation(request);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<FormationResponse>> getFormationsByCandidat(@PathVariable Long candidatId) {
        List<FormationResponse> formations = formationService.getFormationsByCandidat(candidatId);
        return ResponseEntity.ok(formations); // 200 OK
    }
    @GetMapping()
    public ResponseEntity<List<FormationResponse>> getAllFormations() {
        List<FormationResponse> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations); // 200 OK
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFormation(@PathVariable Long id, @RequestBody @Valid FormationRequest request) {
        try {
            formationService.updateFormation(id, request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    // Supprimer une formation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        try {
            formationService.deleteFormation(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }

    // Récupérer une formation par ID
    @GetMapping("/{id}")
    public ResponseEntity<FormationResponse> getFormationById(@PathVariable Long id) {
        try {
            FormationResponse formation = formationService.getFormationById(id);
            return ResponseEntity.ok(formation); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
}