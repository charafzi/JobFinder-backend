package com.ilisi.jobfinder.repository.specification;

import com.ilisi.jobfinder.Enum.StatusOffre;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.model.OffreEmploi;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

            if(searchDTO.getKeyword() != null && StringUtils.hasText(searchDTO.getKeyword())) {
                String keyword = "%" + searchDTO.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("titre")), keyword),
                    cb.like(cb.lower(root.get("description")), keyword),
                    cb.like(cb.lower(root.get("poste")), keyword),
                    cb.like(cb.lower(root.joinList("exigences", JoinType.LEFT)), keyword)
                ));
            }

            if (searchDTO.getTypeContrat() != null) {
                predicates.add(cb.equal(root.get("typeContrat"), searchDTO.getTypeContrat()));
            }

            if (searchDTO.getSalaryMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaire"), searchDTO.getSalaryMin()));
            }
            if (searchDTO.getSalaryMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaire"), searchDTO.getSalaryMax()));
            }

            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}