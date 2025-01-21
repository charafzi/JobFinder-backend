package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.Langue.LangueRequest;
import com.ilisi.jobfinder.dto.Langue.LangueResponse;
import com.ilisi.jobfinder.model.Langue;

public class LangueMapper {
    public static Langue toEntity(LangueRequest request) {
        Langue langue = new Langue();
        langue.setNomLangue(request.getNomLangue());
        langue.setNiveau(request.getNiveau());
        return langue;
    }

    public static LangueResponse toLangueResponse(Langue langue) {
        return new LangueResponse(
            langue.getId(),
            langue.getNomLangue(),
            langue.getNiveau(),
            langue.getCandidat().getId()
        );
    }
}
