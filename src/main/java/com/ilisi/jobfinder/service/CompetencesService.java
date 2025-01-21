package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.dto.Competences.CompetencesRequest;
import com.ilisi.jobfinder.dto.Competences.CompetencesResponse;
import com.ilisi.jobfinder.mapper.CompetencesMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Competences;
import com.ilisi.jobfinder.repository.CandidatRepository;
import com.ilisi.jobfinder.repository.CompetencesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompetencesService {
    private final CompetencesRepository competencesRepository;
    private final CandidatRepository candidatRepository;

    public void createCompetences(CompetencesRequest request) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        Candidat candidat = candidatOpt.get();
        Competences competences = CompetencesMapper.toEntity(request);
        competences.setCandidat(candidat);
        competencesRepository.save(competences);
    }

    public List<CompetencesResponse> getCompetencesByCandidat(Long candidatId) {
        List<Competences> competences = competencesRepository.findAllByCandidatId(candidatId);
        return competences
                .stream()
                .map(CompetencesMapper::toCompetencesResponse)
                .collect(Collectors.toList());
    }

    public void updateCompetences(Long id, CompetencesRequest request) {
        Competences competences = competencesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competences introuvable !"));
        
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        
        Candidat candidat = candidatOpt.get();
        competences.setNomCompetence(request.getNomCompetence());
        competences.setCandidat(candidat);
        
        competencesRepository.save(competences);
    }

    public void deleteCompetences(Long id) {
        if (!competencesRepository.existsById(id)) {
            throw new EntityNotFoundException("Competences introuvable !");
        }
        competencesRepository.deleteById(id);
    }

    public CompetencesResponse getCompetencesById(Long id) {
        Competences competences = competencesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competences introuvable !"));
        return CompetencesMapper.toCompetencesResponse(competences);
    }

    public List<CompetencesResponse> getAllCompetences() {
        return competencesRepository.findAll()
                .stream()
                .map(CompetencesMapper::toCompetencesResponse)
                .collect(Collectors.toList());
    }
}
