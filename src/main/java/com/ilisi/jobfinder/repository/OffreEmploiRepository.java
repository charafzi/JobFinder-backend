package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.OffreEmploi;
import com.nimbusds.jose.util.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi,Long>, JpaSpecificationExecutor<OffreEmploi>{

    Page<OffreEmploi> findAll(Specification<OffreEmploi> spec, Pageable pageable);
}
