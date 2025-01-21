package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.dto.Formation.FormationRequest;
import com.ilisi.jobfinder.dto.Formation.FormationResponse;
import com.ilisi.jobfinder.mapper.FormationMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Formation;
import com.ilisi.jobfinder.repository.CandidatRepository;
import com.ilisi.jobfinder.repository.FormationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormationService {
    private final FormationRepository formationRepository;
    private final CandidatRepository candidatRepository;

    public void createFormation(FormationRequest request) {
        // Récupérer le candidat associé
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        Candidat candidat = candidatOpt.get();
        //Convertir le FormationRequest en Formation et associer le candidat
        Formation formation = FormationMapper.toEntity(request);
        formation.setCandidat(candidat);
        // Sauvegarder la formation
        formationRepository.save(formation);
    }

    public List<FormationResponse> getFormationsByCandidat(Long candidatId) {
        List<Formation> formations=formationRepository.findAllByCandidatId(candidatId);
        return formations
                .stream()
                .map(FormationMapper::toFormationResponse)
                .collect(Collectors.toList());
    }

    public void updateFormation(Long id, FormationRequest request) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formation not found !!"));
        formation.setNomEcole(request.getNomEcole());
        formation.setNiveauEtude(request.getNiveauEtude());
        formation.setDateDebut(request.getDateDebut());
        formation.setDateFin(request.getDateFin());
        formationRepository.save(formation);
    }

    public void deleteFormation(Long id) {
        formationRepository.deleteById(id);
    }

    public FormationResponse getFormationById(Long id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation introuvable !"));
        return FormationMapper.toFormationResponse(formation);
    }

    public List<FormationResponse> getAllFormations() {
        List<Formation> formations=formationRepository.findAll();
        return formations
                .stream()
                .map(FormationMapper::toFormationResponse)
                .collect(Collectors.toList());
    }
}
