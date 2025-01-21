package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.About.AboutRequest;
import com.ilisi.jobfinder.dto.About.AboutResponse;
import com.ilisi.jobfinder.model.About;
import com.ilisi.jobfinder.model.Candidat;
import org.springframework.stereotype.Component;

@Component
public class AboutMapper {
    
    public static About toEntity(AboutRequest request) {
        About about = new About();
        about.setDescription(request.getDescription());
        return about;
    }

    public static AboutResponse toResponse(About about) {
        return new AboutResponse(
            about.getId(),
            about.getDescription(),
            about.getCandidat().getId()
        );
    }
}
