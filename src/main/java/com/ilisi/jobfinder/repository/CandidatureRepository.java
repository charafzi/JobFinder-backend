package com.ilisi.jobfinder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import com.ilisi.jobfinder.model.CandidatureId;
import com.ilisi.jobfinder.model.OffreEmploi;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, CandidatureId> {

    Optional<Candidature> findByCandidatAndOffreEmploi(Candidat candidat, OffreEmploi offreEmploi);
    List<Candidature> findCandidaturesByOffreEmploiId(Long offreId);
    Page<Candidature> findCandidatureByCandidatId(Long candidatId, Pageable pageable);
}
