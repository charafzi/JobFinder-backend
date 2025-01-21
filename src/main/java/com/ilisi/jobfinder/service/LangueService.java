package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.dto.Langue.LangueRequest;
import com.ilisi.jobfinder.dto.Langue.LangueResponse;
import com.ilisi.jobfinder.mapper.LangueMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Langue;
import com.ilisi.jobfinder.repository.CandidatRepository;
import com.ilisi.jobfinder.repository.LangueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LangueService {
    private final LangueRepository langueRepository;
    private final CandidatRepository candidatRepository;

    public void createLangue(LangueRequest request) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        Candidat candidat = candidatOpt.get();
        Langue langue = LangueMapper.toEntity(request);
        langue.setCandidat(candidat);
        langueRepository.save(langue);
    }

    public List<LangueResponse> getLanguesByCandidat(Long candidatId) {
        List<Langue> langues = langueRepository.findAllByCandidatId(candidatId);
        return langues
                .stream()
                .map(LangueMapper::toLangueResponse)
                .collect(Collectors.toList());
    }

    public void updateLangue(Long id, LangueRequest request) {
        Langue langue = langueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable !"));
        
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        
        Candidat candidat = candidatOpt.get();
        langue.setNomLangue(request.getNomLangue());
        langue.setNiveau(request.getNiveau());
        langue.setCandidat(candidat);
        
        langueRepository.save(langue);
    }

    public void deleteLangue(Long id) {
        if (!langueRepository.existsById(id)) {
            throw new EntityNotFoundException("Langue introuvable !");
        }
        langueRepository.deleteById(id);
    }

    public LangueResponse getLangueById(Long id) {
        Langue langue = langueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Langue introuvable !"));
        return LangueMapper.toLangueResponse(langue);
    }

    public List<LangueResponse> getAllLangues() {
        return langueRepository.findAll()
                .stream()
                .map(LangueMapper::toLangueResponse)
                .collect(Collectors.toList());
    }
}
