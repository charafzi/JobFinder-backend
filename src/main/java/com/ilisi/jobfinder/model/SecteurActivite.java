package com.ilisi.jobfinder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecteurActivite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @ManyToMany(mappedBy = "secteurActivites")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Entreprise> entrepriseList;
}
