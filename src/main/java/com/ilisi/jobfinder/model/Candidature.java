package com.ilisi.jobfinder.model;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidature {
    @EmbeddedId
    private CandidatureId id;

    @ManyToOne
    @MapsId("candidatId")
    @JoinColumn(name = "candidat_id")
    private Candidat candidat;

    @ManyToOne
    @MapsId("offreEmploiId")
    @JoinColumn(name = "offre_id")
    private OffreEmploi offreEmploi;

    @Enumerated(EnumType.STRING)
    private CandidatureStatus status;

    private LocalDateTime dateCandidature;
}
