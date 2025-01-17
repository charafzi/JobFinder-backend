package com.ilisi.jobfinder.dto.OffreEmploi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.dto.AdresseDTO;
import com.ilisi.jobfinder.dto.EntrepriseDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class OffreSearchResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String position;
    private List<String> requirements;
    private ContratType contractType;
    private double salary;
    private String question;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publicationDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deadlineDate;
    private String timeAgo;
    private EntrepriseDTO company;
    private AdresseDTO adress;
}
