package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.model.Entreprise;

public class EntrepriseMapper {
    public static EntrepriseDTO toDto(Entreprise entity){
        return EntrepriseDTO.builder()
                .id(entity.getId())
                .name(entity.getNom())
                .email(entity.getEmail())
                .adress(AdresseMapper.toDto(entity.getAdresse()))
                .profilePicture(entity.getPhotoProfile())
                .phoneNumber(entity.getTelephone())
                .activitySectors(entity.getSecteurActivites())
                .role(Role.ENTREPRISE)
                .about(entity.getAbout())
                .fcmToken(entity.getFcmToken())
                .build();
    }
}
