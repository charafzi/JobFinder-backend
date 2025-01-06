package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.OffreEmploi;
import com.nimbusds.jose.util.Resource;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi,Integer>, JpaSpecificationExecutor<OffreEmploi>{

    Page<OffreEmploi> findAll(Specification<OffreEmploi> spec, Pageable pageable);

    @Query(value = "" +
            "SELECT * " +
            "FROM offre_emploi o, adresse a " +
            "where ST_DistanceSphere(a.coordinates, :p) < :radius " +
            "and o.adresse_adresse_id = a.adresse_id"
            ,nativeQuery = true)
    List<OffreEmploi> findOffresWithinRadius(Point p,double radius);
}
