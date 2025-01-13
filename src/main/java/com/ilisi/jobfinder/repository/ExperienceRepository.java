package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience,Long> {
    List<Experience> findAllByCandidatId(Long candidatId);
}
