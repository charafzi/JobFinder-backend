package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.About.AboutRequest;
import com.ilisi.jobfinder.dto.About.AboutResponse;
import com.ilisi.jobfinder.dto.Formation.FormationRequest;
import com.ilisi.jobfinder.dto.Formation.FormationResponse;
import com.ilisi.jobfinder.service.AboutService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/abouts")
public class AboutController {
    private final AboutService aboutService;

    @PostMapping
    public ResponseEntity<String> createAbout(@RequestBody @Valid AboutRequest request) {
        try {
            aboutService.createAbout(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la création d'un About.");
        }
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<AboutResponse>> getAboutsByCandidatId(@PathVariable Long candidatId) {
        List<AboutResponse> abouts = aboutService.getAboutByCandidatId(candidatId);
        return ResponseEntity.ok(abouts); // 200 OK
    }

    @GetMapping()
    public ResponseEntity<List<AboutResponse>> getAllAbouts() {
        List<AboutResponse> aboutResponses = aboutService.getAllAbouts();
        return ResponseEntity.ok(aboutResponses); // 200 OK
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAbout(@PathVariable Long id, @RequestBody @Valid AboutRequest request) {
        try {
            aboutService.updateAbout(id, request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<AboutResponse> getAboutById(@PathVariable Long id) {
        try {
            AboutResponse aboutResponse = aboutService.getAboutById(id);
            return ResponseEntity.ok(aboutResponse); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAbout(@PathVariable Long id) {
        aboutService.deleteAbout(id);
    }
}
