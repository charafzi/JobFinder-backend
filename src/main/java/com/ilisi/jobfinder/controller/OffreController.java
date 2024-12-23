package com.ilisi.jobfinder.controller;


import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.service.OffreEmploiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/offre")
public class OffreController {
    private OffreEmploiService offreEmploiService;

    @PostMapping
    public ResponseEntity<OffreEmploi> createOffre(@RequestBody OffreEmploi offreEmploi) {
        try {
            // Appeler le service pour créer l'offre
            OffreEmploi savedOffre = offreEmploiService.create_Offre(offreEmploi);
            return ResponseEntity.ok(savedOffre);
        } catch (RuntimeException e) {
            // En cas d'erreur (par exemple, entreprise introuvable)
            System.out.println("Error creating offre: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    //Tous les offres
    @GetMapping
    public ResponseEntity<List<OffreEmploi>> getAllOffers(){
        return ResponseEntity.ok(offreEmploiService.getAllOffres());
    }

    //getOffreBy Id
    @GetMapping("/{id}")
    public ResponseEntity<OffreEmploi> getOffreById(@PathVariable int id){
        return offreEmploiService.getOffreById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //modifier une offre
    @PutMapping("/{id}")
    public ResponseEntity<OffreEmploi> updateOffre(@PathVariable int id,@RequestBody OffreEmploi updatedOffre){
        try {
            return ResponseEntity.ok(offreEmploiService.updateOffre(id, updatedOffre));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable int id){
        try {
            offreEmploiService.deleteOffre(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }





    }



