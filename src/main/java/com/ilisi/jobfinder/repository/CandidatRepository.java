package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat,Long> {
}
