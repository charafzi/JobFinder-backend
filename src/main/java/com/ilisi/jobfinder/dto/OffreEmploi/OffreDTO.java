package com.ilisi.jobfinder.dto.OffreEmploi;

import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.Enum.StatusOffre;
import com.ilisi.jobfinder.dto.AdresseDTO;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OffreDTO {
    private Long id;
    private String title;
    private String description;
    private String position;
    private List<String> requirements;
    private ContratType contractType;
    private double salary;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadlineDate;

    private StatusOffre status;
    private OffreDTO.Entreprise company;
    private AdresseDTO adress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Entreprise{
        private Long id;
        private String email;
        private String phoneNumber;
        private String name;
    }
}
