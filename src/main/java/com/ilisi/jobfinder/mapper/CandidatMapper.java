package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.CandidatDTO;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.model.Candidat;

public class CandidatMapper {
    public static CandidatDTO toDto(Candidat entity){
        return CandidatDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getPrenom())
                .lastName(entity.getNom())
                .profilePicture(entity.getPhotoProfile())
                .phoneNumber(entity.getTelephone())
                .role(Role.CANDIDAT)
                .build();
    }
}
