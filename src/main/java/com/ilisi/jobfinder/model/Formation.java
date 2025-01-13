package com.ilisi.jobfinder.model;

import com.ilisi.jobfinder.Enum.NiveauEtudeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomEcole;
    @Enumerated(EnumType.STRING)
    private NiveauEtudeStatus niveauEtude;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
}
