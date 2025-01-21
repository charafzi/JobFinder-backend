package com.ilisi.jobfinder.mapper;

import com.ilisi.jobfinder.Enum.DocumentType;
import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.dto.CandidatDTO;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Document;

import java.util.List;

public class CandidatMapper {
    public static CandidatDTO toDto(Candidat entity){
        List<Long> cvIds = entity.getDocuments().stream()
                .filter(document -> document.getFileType() == DocumentType.CV)
                .map(Document::getId)
                .toList();

        return CandidatDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getPrenom())
                .lastName(entity.getNom())
                .profilePicture(entity.getPhotoProfile())
                .phoneNumber(entity.getTelephone())
                .role(Role.CANDIDAT)
                .cvDocumentsId(cvIds)
                .fcmToken(entity.getFcmToken())
                .build();
    }
}
