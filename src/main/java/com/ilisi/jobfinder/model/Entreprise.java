package com.ilisi.jobfinder.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Entreprise extends User {
    private String nom;
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OffreEmploi> offres;
    @OneToOne(cascade = CascadeType.ALL)
    private Adresse adresse;
    @ManyToMany
    @JoinTable(
            name = "entreprise_secteur",
            joinColumns = @JoinColumn(name = "entreprise_id"),
            inverseJoinColumns = @JoinColumn(name = "secteur_id")
    )
    private List<SecteurActivite> secteurActivites;
}
