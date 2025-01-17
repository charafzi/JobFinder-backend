package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.Candidature;
import com.ilisi.jobfinder.model.CandidatureId;
import com.ilisi.jobfinder.model.OffreEmploi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, CandidatureId> {

    Optional<Candidature> findByCandidatAndOffreEmploi(Candidat candidat, OffreEmploi offreEmploi);
    Page<Candidature> findCandidaturesByOffreEmploiId(Long offreId, Pageable pageable);
    List<Candidature> findCandidatureByCandidatId(Long candidatId);
}
