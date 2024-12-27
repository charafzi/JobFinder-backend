package com.ilisi.jobfinder.dto.OffreEmploi;

import com.ilisi.jobfinder.Enum.ContratType;
import com.ilisi.jobfinder.Enum.SortBy;
import com.ilisi.jobfinder.Enum.SortDirection;
import lombok.Data;

@Data
public class OffreSearchRequestDTO {
    private String keyword;
    private ContratType typeContrat;
    private Double salaryMin;
    private Double salaryMax;
    private int page = 0;
    private int size = 10;
    private SortBy sortBy = SortBy.PUB_DATE;
    private SortDirection sortDirection = SortDirection.ASC;
}