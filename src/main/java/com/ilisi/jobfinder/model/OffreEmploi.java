package com.ilisi.jobfinder.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.Enum.StatusOffre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OffreEmploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private String poste;
    @ElementCollection
    private List<String> exigences;
    @Enumerated(EnumType.STRING)
    private ContratType typeContrat;
    private double salaire;
    private LocalDateTime datePublication;
    private LocalDateTime dateLimite;
    @Enumerated(EnumType.STRING)
    private StatusOffre statusOffre;
    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;
    @ManyToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    @OneToMany(mappedBy = "offreEmploi")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Candidature> candidatures;
}
