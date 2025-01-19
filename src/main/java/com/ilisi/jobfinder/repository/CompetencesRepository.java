package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Competences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetencesRepository extends JpaRepository<Competences, Long> {
    List<Competences> findAllByCandidatId(Long candidatId);
}
