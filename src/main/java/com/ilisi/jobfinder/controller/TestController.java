package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.CandidatureRepository;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private UserRepository userRepository;
    private OffreEmploiRepository emploiRepository;
    private CandidatureRepository candidatureRepository;

    @GetMapping
    public ResponseEntity<?> protectedEndPoint(){
        Optional<OffreEmploi> optional = this.emploiRepository.findById(4);
        if (optional.isEmpty()){
            return ResponseEntity.internalServerError().body("NO emploi");
        }
        Optional<User> userOptional = this.userRepository.findById(6L);
        if(userOptional.isEmpty()){
            return ResponseEntity.internalServerError().body("NO user");

        }

        /*CandidatureId candidatureId = new CandidatureId();
        candidatureId.setCandidatId(userOptional.get().getId()); // Set candidatId from Candidat
        candidatureId.setOffreEmploiId(optional.get().getId());

        Candidature candidature = new Candidature();
        candidature.setId(candidatureId);
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatus(CandidatureStatus.ENVOYEE);
        candidature.setCandidat((Candidat) userOptional.get());
        candidature.setOffreEmploi(optional.get());
        this.candidatureRepository.save(candidature);*/
        List<Candidature> candidatures = this.candidatureRepository.findAll();
        return ResponseEntity.ok().body(candidatures);
    }


}
