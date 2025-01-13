package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation,Long> {

    List<Formation> findAllByCandidatId(Long candidatId);
}
