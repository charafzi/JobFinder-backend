package com.ilisi.jobfinder.controller;


import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.Enum.SortBy;
import com.ilisi.jobfinder.Enum.SortDirection;
import com.ilisi.jobfinder.dto.Candidature.CandidatureCandidatDTO;
import com.ilisi.jobfinder.dto.Candidature.CandidatureDeleteRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureStatusUpdateResquest;
import com.ilisi.jobfinder.dto.Candidature.GetCandidaturesByOffreDTO;
import com.ilisi.jobfinder.dto.CandidatureDTO;

import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.exceptions.AucuneReponsePourQuestion;
import com.ilisi.jobfinder.exceptions.OffreDejaPostule;
import com.ilisi.jobfinder.service.CandidatureService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidature")
@AllArgsConstructor
public class CandidatureController {
    private final CandidatureService candidatureService;

    @PostMapping(value = "/postuler", consumes = "multipart/form-data")
    public ResponseEntity<String> postulerCandidature(@ModelAttribute @Valid CandidatureRequest request)
    {
        try {
            candidatureService.postuler(request);
            return ResponseEntity.ok("Candidature soumise avec succès !");
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
        catch (OffreDejaPostule | AucuneReponsePourQuestion e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());

        }
    }

    @GetMapping("/{offreId}")
    public ResponseEntity<PageResponse<CandidatureDTO>> getAllCandidaturesByOffre(
            @PathVariable Long offreId,
            @ModelAttribute GetCandidaturesByOffreDTO searchDTO) {

        try {
            searchDTO.setOffreId(offreId);
            Page<CandidatureDTO> candidatureDTOS = this.candidatureService.getAllCandidaturesByOffre(searchDTO);
            return ResponseEntity.ok(PageResponse.of(candidatureDTOS));
        } catch (EntityNotFoundException e) {
            // Pour une ressource non trouvée, retourner 404
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log l'erreur ici si nécessaire
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check/{userId}/{offreId}")
    public ResponseEntity<Boolean> checkIfUserApplied(@PathVariable Long userId,@PathVariable Long offreId) {
        try {
            boolean applied = this.candidatureService.checkIfUserApplied(userId, offreId);
            return  ResponseEntity.ok(applied);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/")
    public ResponseEntity<PageResponse<CandidatureCandidatDTO>> getAllCandidaturesByUser(
            @RequestParam Long id,
            @RequestParam int page,
            @RequestParam int size
            ){
        try {
            PageResponse<CandidatureCandidatDTO> result= this.candidatureService.getAllCandidaturesByUser(id,page,size);
            return ResponseEntity.ok().body(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}/{offreId}")
    public ResponseEntity<?> deleteCandidatureByUser(
            @PathVariable Long userId,
            @PathVariable Long offreId
            ){
        try {
            this.candidatureService.deleteCandidature(userId,offreId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/accept")
    ResponseEntity<String> accpetCandidatureById(
            @RequestBody CandidatureStatusUpdateResquest request){
        try {
            this.candidatureService.changeCandidatureStatus(request, CandidatureStatus.ACCEPTE);
            return ResponseEntity.ok("Candidature acceptée avec succées");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/dismiss")
    ResponseEntity<String> dismissCandidatureById(
            @RequestBody CandidatureStatusUpdateResquest request){
        try {
            this.candidatureService.changeCandidatureStatus(request, CandidatureStatus.REJETEE);
            return ResponseEntity.ok("Candidature rejetée avec succées");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/entreprises/{entrepriseId}/count")
    public ResponseEntity<Integer> getNombreCandidaturesParEntreprise(@PathVariable Long entrepriseId) {
        int nombreCandidatures = candidatureService.getNombreCandidaturesParEntreprise(entrepriseId);
        return ResponseEntity.ok(nombreCandidatures);
    }

    @GetMapping("/entreprises/{entrepriseId}/accepted/count")
    public ResponseEntity<Integer> getNombreCandidaturesAccepteesParEntreprise(@PathVariable Long entrepriseId) {
        int nombreCandidaturesAcceptees = candidatureService.getNombreCandidaturesAccepteesParEntreprise(entrepriseId);
        return ResponseEntity.ok(nombreCandidaturesAcceptees);
    }
}
