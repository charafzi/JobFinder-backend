package com.ilisi.jobfinder.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class CandidatureId implements Serializable {
    private Long candidatId;
    private Long offreEmploiId;
}