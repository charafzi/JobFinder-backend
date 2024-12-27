package com.ilisi.jobfinder.repository.specification;

import com.ilisi.jobfinder.Enum.StatusOffre;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.model.Entreprise;
import com.ilisi.jobfinder.model.OffreEmploi;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OffreSpecification {

    public static Specification<OffreEmploi> buildSpecification(OffreSearchRequestDTO searchDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<OffreEmploi, Entreprise> entrepriseJoin = root.join("entreprise");

            if (StringUtils.hasText(searchDTO.getKeyword())) {
                String keyword = "%" + searchDTO.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("titre")), keyword),
                        cb.like(cb.lower(root.get("description")), keyword),
                        cb.like(cb.lower(root.get("poste")), keyword),
                        cb.like(cb.lower(root.join("exigences")), keyword)
                ));

            }

            /*if (StringUtils.hasText(searchDTO.getLocation())) {
                String location = "%" + searchDTO.getLocation().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(entrepriseJoin.get("adresse")), location));
            }*/

            if (searchDTO.getTypeContrat() != null) {
                predicates.add(cb.equal(root.get("typeContrat"), searchDTO.getTypeContrat()));
            }

            if (searchDTO.getSalaryMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaire"), searchDTO.getSalaryMin()));
            }

            if (searchDTO.getSalaryMax()!= null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaire"), searchDTO.getSalaryMax()));
            }

            //get only active Offers
            predicates.add(cb.equal(root.get("statusOffre"), StatusOffre.active));

            /*if (StringUtils.hasText(searchDTO.getEntrepriseNom())) {
                String nom = "%" + searchDTO.getEntrepriseNom().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(entrepriseJoin.get("nom")), nom));
            }*/

            predicates.add(cb.greaterThanOrEqualTo(root.get("dateLimite"), LocalDateTime.now()));

            /*if (searchDTO.getLastId() != null) {
                if ("DESC".equals(searchDTO.getSortDirection())) {
                    predicates.add(cb.lessThan(root.get("id"), searchDTO.getLastId()));
                } else {
                    predicates.add(cb.greaterThan(root.get("id"), searchDTO.getLastId()));
                }
            }*/

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}