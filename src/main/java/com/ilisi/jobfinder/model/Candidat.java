package com.ilisi.jobfinder.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Candidat extends User {
    private String prenom;
    private String nom;
    @OneToMany(mappedBy = "candidat")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Candidature> candidatures;
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formation> formations;
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;
    @OneToMany(mappedBy ="candidat", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Document> documents;
}
