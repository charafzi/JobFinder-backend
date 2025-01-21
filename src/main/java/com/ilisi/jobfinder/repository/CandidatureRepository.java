package com.ilisi.jobfinder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import com.ilisi.jobfinder.model.CandidatureId;
import com.ilisi.jobfinder.model.OffreEmploi;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, CandidatureId> {

    Optional<Candidature> findByCandidatAndOffreEmploi(Candidat candidat, OffreEmploi offreEmploi);
    Page<Candidature> findCandidaturesByOffreEmploiId(Long offreId, Pageable pageable);
    Page<Candidature> findCandidatureByCandidatId(Long candidatId, Pageable pageable);

    @Query("SELECT count(c) FROM Candidature c WHERE c.offreEmploi.entreprise.id= :entrepriseId")
    int countByEntrepriseId(@Param("entrepriseId") Long entrepriseId);

    @Query("SELECT count(c) FROM Candidature c WHERE c.offreEmploi.entreprise.id= :entrepriseId and c.status = 'ACCEPTE' ")
    int countAcceptedCandidaturesByEntrepriseId(@Param("entrepriseId") Long entrepriseId);
}
