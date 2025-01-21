package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepriseRepository extends JpaRepository<Entreprise,Long> {
}
