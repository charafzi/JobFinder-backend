package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.About;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AboutRepository extends JpaRepository<About, Long> {
    boolean existsByCandidatId(Long candidatId);

    List<About> findAllByCandidatId(Long candidatId);
}
