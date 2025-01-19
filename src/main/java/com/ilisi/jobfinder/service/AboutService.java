package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.dto.About.AboutRequest;
import com.ilisi.jobfinder.dto.About.AboutResponse;
import com.ilisi.jobfinder.dto.Formation.FormationResponse;
import com.ilisi.jobfinder.mapper.AboutMapper;
import com.ilisi.jobfinder.mapper.FormationMapper;
import com.ilisi.jobfinder.model.About;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Formation;
import com.ilisi.jobfinder.repository.AboutRepository;
import com.ilisi.jobfinder.repository.CandidatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AboutService {
    private final AboutRepository aboutRepository;
    private final CandidatRepository candidatRepository;
    private final AboutMapper aboutMapper;

    public List<AboutResponse> getAllAbouts() {
        return aboutRepository.findAll()
                .stream()
                .map(AboutMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<AboutResponse> getAboutByCandidatId(Long candidatId) {
        List<About> aboutResponses=aboutRepository.findAllByCandidatId(candidatId);
        return aboutResponses
                .stream()
                .map(AboutMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void createAbout(AboutRequest request) {
        Candidat candidat = candidatRepository.findById(request.getCandidatId())
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable !"));
        // Vérifier si le candidat a déjà un About
        if (aboutRepository.existsByCandidatId(request.getCandidatId())) {
            throw new IllegalStateException("Le candidat a déjà un About");
        }
        About about = aboutMapper.toEntity(request);
        about.setCandidat(candidat);
        aboutRepository.save(about);
    }

    public void updateAbout(Long id, AboutRequest request) {
        About existingAbout = aboutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("About not found"));

        existingAbout.setDescription(request.getDescription());
        aboutRepository.save(existingAbout);
    }

    public void deleteAbout(Long id) {
        About about = aboutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("About with ID " + id + " not found"));

        // Suppression de l'About
        aboutRepository.delete(about);

        // Vérification après suppression
        if (aboutRepository.existsById(id)) {
            throw new RuntimeException("Suppression échouée pour About avec ID " + id);
        }
    }

    public AboutResponse getAboutById(Long id) {
        About about = aboutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("About introuvable !"));
        return AboutMapper.toResponse(about);
    }
}