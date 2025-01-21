package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.Langue.LangueRequest;
import com.ilisi.jobfinder.dto.Langue.LangueResponse;
import com.ilisi.jobfinder.service.LangueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/langues")
public class LangueController {
    private final LangueService langueService;

    @PostMapping
    public ResponseEntity<Void> createLangue(@RequestBody @Valid LangueRequest request) {
        try {
            langueService.createLangue(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<LangueResponse>> getLanguesByCandidat(@PathVariable Long candidatId) {
        List<LangueResponse> langues = langueService.getLanguesByCandidat(candidatId);
        return ResponseEntity.ok(langues);
    }

    @GetMapping
    public ResponseEntity<List<LangueResponse>> getAllLangues() {
        List<LangueResponse> langues = langueService.getAllLangues();
        return ResponseEntity.ok(langues);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LangueResponse> getLangueById(@PathVariable Long id) {
        try {
            LangueResponse langue = langueService.getLangueById(id);
            return ResponseEntity.ok(langue);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLangue(@PathVariable Long id, @RequestBody @Valid LangueRequest request) {
        try {
            langueService.updateLangue(id, request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLangue(@PathVariable Long id) {
        try {
            langueService.deleteLangue(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
