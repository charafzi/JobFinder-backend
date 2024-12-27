package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.dto.EntrepriseDTO;
import com.ilisi.jobfinder.model.Entreprise;

public class EntrepriseMapper {

    public static EntrepriseDTO toDto(Entreprise entity){
        return EntrepriseDTO.builder()
                .id(entity.getId())
                .nom(entity.getNom())
                .email(entity.getEmail())
                .adresse(AdresseMapper.toDto(entity.getAdresse()))
                .photoProfile(entity.getPhotoProfile())
                .telephone(entity.getTelephone())
                .build();
    }
}
