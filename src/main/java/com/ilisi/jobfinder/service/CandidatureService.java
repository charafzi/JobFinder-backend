package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.Candidature.CandidatureCandidatDTO;
import com.ilisi.jobfinder.dto.Candidature.CandidatureDeleteRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.dto.Candidature.CandidatureStatusUpdateResquest;
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
import java.util.Optional;

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

    public List<CandidatureCandidatDTO> getAllCandidaturesByUser(String email) throws EntityNotFoundException{
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        List<Candidature> candidatures = candidat.getCandidatures();
        return candidatures.stream().map(CandidatureMapper::toCandidatureCandidatDTO).toList();
    }

    public void deleteCandidature(CandidatureDeleteRequest request) throws EntityNotFoundException {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        Candidature candidature = candidat.getCandidatures()
                .stream()
                .filter(candid -> candid.getOffreEmploi().getId().equals(request.getOffreId()))
                .findFirst()
                .orElse(null);

        if(candidature == null){
            throw new EntityNotFoundException("Candidature introuvable pour offre ="+request.getOffreId());
        }

        candidat.getCandidatures().remove(candidature);
        candidatRepository.save(candidat);
    }

    public void changeCandidatureStatus(CandidatureStatusUpdateResquest request, CandidatureStatus candidatureStatus) throws EntityNotFoundException {
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
        Optional<Candidature> candidature = candidatureRepository.findById(candidatureId);
        if (candidature.isEmpty()) {
            throw new EntityNotFoundException("Candidature introuvable.");
        }
        Candidature candid = candidature.get();

        candid.setStatus(candidatureStatus);
        candidatureRepository.save(candid);
    }
}
