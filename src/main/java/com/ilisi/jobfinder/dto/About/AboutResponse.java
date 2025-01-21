package com.ilisi.jobfinder.dto.About;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutResponse {
    private Long id;
    private String description;
    private Long candidatId;
}
