package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.Candidat;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat,Long> {

    Optional<Candidat> findByEmail( String email);
}
