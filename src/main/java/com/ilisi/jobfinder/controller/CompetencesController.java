package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Competences.CompetencesRequest;
import com.ilisi.jobfinder.dto.Competences.CompetencesResponse;
import com.ilisi.jobfinder.service.CompetencesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/competences")
public class CompetencesController {
    private final CompetencesService competencesService;

    @PostMapping
    public ResponseEntity<Void> createCompetences(@RequestBody @Valid CompetencesRequest request) {
        try {
            competencesService.createCompetences(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<CompetencesResponse>> getCompetencesByCandidat(@PathVariable Long candidatId) {
        List<CompetencesResponse> competences = competencesService.getCompetencesByCandidat(candidatId);
        return ResponseEntity.ok(competences);
    }

    @GetMapping
    public ResponseEntity<List<CompetencesResponse>> getAllCompetences(){
        List<CompetencesResponse> competences = competencesService.getAllCompetences();
        return ResponseEntity.ok(competences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencesResponse> getCompetencesById(@PathVariable Long id) {
        try {
            CompetencesResponse competences = competencesService.getCompetencesById(id);
            return ResponseEntity.ok(competences);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompetences(@PathVariable Long id, @RequestBody @Valid CompetencesRequest request) {
        try {
            competencesService.updateCompetences(id, request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetences(@PathVariable Long id) {
        try {
            competencesService.deleteCompetences(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
