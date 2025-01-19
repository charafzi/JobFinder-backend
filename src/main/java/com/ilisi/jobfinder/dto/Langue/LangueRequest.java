package com.ilisi.jobfinder.dto.Langue;

import com.ilisi.jobfinder.Enum.NiveauLangue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LangueRequest {
    private String nomLangue;
    private NiveauLangue niveau;
    private Long candidatId;
}
