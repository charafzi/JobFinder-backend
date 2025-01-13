package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.dto.Candidature.CandidatureRequest;
import com.ilisi.jobfinder.mapper.CandidatureMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import com.ilisi.jobfinder.model.Document;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.repository.CandidatRepository;
import com.ilisi.jobfinder.repository.CandidatureRepository;
import com.ilisi.jobfinder.repository.DocumentRepository;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CandidatureService {
    private final CandidatRepository candidatRepository;
    private final OffreEmploiRepository offreEmploiRepository;
    private final CandidatureRepository candidatureRepository;
    private final DocumentRepository documentRepository;

    public void postuler(CandidatureRequest request) {
        // Vérifier si le candidat existe
        Candidat candidat = candidatRepository.findById(request.getCandidatId())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable."));

        // Vérifier si l'offre existe
        OffreEmploi offre = offreEmploiRepository.findById(request.getOffreId())
                .orElseThrow(() -> new EntityNotFoundException("Offre introuvable."));

        if (candidatureRepository.findByCandidatAndOffreEmploi(candidat, offre).isPresent()) {
            throw new RuntimeException("Vous avez déjà postulé à cette offre.");
        }

        // Sauvegarder le document (CV ou lettre)
        try {
            saveDocument(candidat, request.getDocument(), request.getDocumentType());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier : " + e.getMessage());
        }

        // Créer et sauvegarder la candidature
        Candidature candidature = CandidatureMapper.toCandidature(request);
        candidatureRepository.save(candidature);
    }

    private void saveDocument(Candidat candidat, MultipartFile file, DocumentType type) throws IOException {
        String storagePath = "C:/Users/HP/Documents/UploadsCV_JobFinder";
        String filePath = storagePath + "/" + file.getOriginalFilename();

        // Sauvegarder le fichier sur le disque
        Files.createDirectories(Paths.get(storagePath));
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        // Créer l'entité Document
        Document document = new Document();
        document.setFilePath(filePath);
        document.setFileType(type);
        document.setUploadDate(LocalDateTime.now());
        document.setCandidat(candidat);
        documentRepository.save(document);
    }
}
