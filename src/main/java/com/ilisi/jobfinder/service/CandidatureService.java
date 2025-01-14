package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.CandidatureDTO;
import com.ilisi.jobfinder.exceptions.OffreDejaPostule;
import com.ilisi.jobfinder.mapper.CandidatureMapper;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CandidatureService {
    private final CandidatRepository candidatRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;
    private final DocumentService documentService;


    public void postuler(CandidatureRequest request) throws IOException, OffreDejaPostule,EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(request.getOffreId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        // Check if already applied - using the composite key
        CandidatureId candidatureId = new CandidatureId();
        candidatureId.setCandidatId(candidat.getId());
        candidatureId.setOffreEmploiId(request.getOffreId());

        if (candidatureRepository.findById(candidatureId).isPresent()) {
            throw new OffreDejaPostule();
        }


        Candidature candidature = new Candidature();
        candidature.setId(candidatureId);
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setOffreEmploi(offre);
        candidature.setCandidat(candidat);


        //Voir si un nouveau CV est téléchargée
        if(request.getNewCv() != null){
            Document newCV = this.documentService.saveDocument(candidat,request.getNewCv(),DocumentType.CV);
            candidature.setCv(newCV);
        }else {
            // get Candidat Cv
            Document cvById = candidat.getDocuments().stream()
                    .filter(document -> document.getId().equals(request.getCvId()))
                    .findFirst()
                    .orElse(null);

            if(cvById == null){
                throw new RuntimeException("Candidat n'a aucun CV avec Id="+request.getCvId());
            }
            candidature.setCv(cvById);
        }

        if(request.getLettreMotivation() != null){
            Document lettreMotivation = this.documentService.saveDocument(candidat,request.getLettreMotivation(),DocumentType.LETTRE_MOTIVATION);
            candidature.setLettreMotivation(lettreMotivation);
        }
        candidature.setStatus(CandidatureStatus.ENVOYEE);
        candidatureRepository.save(candidature);

    }


    public List<CandidatureDTO> getAllCandidaturesByOffre(Long offreId) {
        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(offreId)
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        List<Candidature> candidatures = this.candidatureRepository.findCandidaturesByOffreEmploiId(offreId);
        return candidatures.stream().map(CandidatureMapper::toDto).toList();
    }
}
