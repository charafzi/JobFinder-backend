package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.Experience.ExperienceRequest;
import com.ilisi.jobfinder.dto.Experience.ExperienceResponse;
import com.ilisi.jobfinder.model.Experience;

public class ExperienceMapper {
    public static Experience toExperience(ExperienceRequest request) {
        Experience experience = new Experience();
        experience.setPoste(request.getPoste());
        experience.setDateDebut(request.getDateDebut());
        experience.setDateFin(request.getDateFin());
        return experience;
    }

    public static ExperienceResponse toExperienceResponse(Experience experience) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getPoste(),
                experience.getDateDebut(),
                experience.getDateFin(),
                experience.getCandidat().getId()
        );
    }
}
