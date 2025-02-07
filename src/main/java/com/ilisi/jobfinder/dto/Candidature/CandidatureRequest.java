package com.ilisi.jobfinder.dto.Candidature;

import com.ilisi.jobfinder.Enum.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidatureRequest {
    @NotNull(message = "L'email du candidat est obligatoire.")
    private String email;
    @NotNull(message = "L'ID de l'offre d'emploi est obligatoire.")
    private Long offreId;
    private Long cvId;                      // Pour sélectionner un CV existant
    private MultipartFile newCv;            // Pour uploader un nouveau CV
    private MultipartFile lettreMotivation;  // Pour la lettre de motivation
    private String reponse;

}
