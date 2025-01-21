package com.ilisi.jobfinder.dto.OffreEmploi;

import com.ilisi.jobfinder.Enum.SortBy;
import com.ilisi.jobfinder.Enum.SortDirection;
import lombok.Data;

import java.util.List;

@Data
public class GetOffresByEntrepriseIdDTO {
    Long entrepriseId;
    private int page = 0;
    private int size = 10;
    private SortBy sortBy = SortBy.PUB_DATE;
    private SortDirection sortDirection = SortDirection.DESC;
}
