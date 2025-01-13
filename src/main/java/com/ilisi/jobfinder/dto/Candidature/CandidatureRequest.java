package com.ilisi.jobfinder.dto.Candidature;

import com.ilisi.jobfinder.Enum.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class CandidatureRequest {
    @NotNull(message = "L'ID du candidat est obligatoire.")
    private Long candidatId;

    @NotNull(message = "L'ID de l'offre d'emploi est obligatoire.")
    private Long offreId;

    @NotNull(message = "Le type de document est obligatoire.")
    private DocumentType documentType; // CV ou LETTRE_MOTIVATION

    @NotNull(message = "Le document est obligatoire.")
    private MultipartFile document; // Le fichier associé (CV ou lettre)
}
