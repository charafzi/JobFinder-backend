package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi,Integer> {

}
