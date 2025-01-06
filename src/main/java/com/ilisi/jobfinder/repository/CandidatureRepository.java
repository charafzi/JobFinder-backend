package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Candidature;
import com.ilisi.jobfinder.model.CandidatureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, CandidatureId> {
}
