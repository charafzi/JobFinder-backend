package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Long> {
    List<Langue> findAllByCandidatId(Long candidatId);
}
