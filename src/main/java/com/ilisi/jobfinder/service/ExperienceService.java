package com.ilisi.jobfinder.service;

import com.ilisi.jobfinder.dto.Experience.ExperienceRequest;
import com.ilisi.jobfinder.dto.Experience.ExperienceResponse;
import com.ilisi.jobfinder.mapper.ExperienceMapper;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Experience;
import com.ilisi.jobfinder.repository.CandidatRepository;
import com.ilisi.jobfinder.repository.ExperienceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExperienceService {
private final ExperienceRepository experienceRepository;
private final CandidatRepository candidatRepository;

    public void createExperience(ExperienceRequest request) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(request.getCandidatId());
        if (candidatOpt.isEmpty()) {
            throw new EntityNotFoundException("Candidat introuvable !");
        }
        Candidat candidat=candidatOpt.get();
        Experience experience= ExperienceMapper.toExperience(request);
        experience.setCandidat(candidat);
        experienceRepository.save(experience);
    }
    public List<ExperienceResponse> getExprienceByCandidat(Long candidatId) {
        List<Experience> experiences=experienceRepository.findAllByCandidatId(candidatId);
        return experiences
                .stream()
                .map(ExperienceMapper::toExperienceResponse)
                .collect(Collectors.toList());
    }
    public void updateExperience(Long id, ExperienceRequest request) {
        //verifier si cette experience exist
        Experience experience=experienceRepository.findById(id)
                        .orElseThrow(()->new EntityNotFoundException("Experience not found !!"));
        experience.setPoste(request.getPoste());
        experience.setDateDebut(request.getDateDebut());
        experience.setDateFin(request.getDateFin());
        experienceRepository.save(experience);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    public ExperienceResponse getExperienceById(Long id) {
       Experience experience=experienceRepository.findById(id)
               .orElseThrow(()->new EntityNotFoundException("Experience not found !!"));

       return ExperienceMapper.toExperienceResponse(experience);

    }

    public List<ExperienceResponse> getAllExperiences() {
        List<Experience> experiences=experienceRepository.findAll();
        return experiences
                .stream()
                .map(ExperienceMapper::toExperienceResponse)
                .collect(Collectors.toList());
    }
}
