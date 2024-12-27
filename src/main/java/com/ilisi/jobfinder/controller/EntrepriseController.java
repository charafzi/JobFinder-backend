package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.service.EntrepriseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO Add picture handling
//TODO Add secteur activites handling in All CRUD operations

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
}
