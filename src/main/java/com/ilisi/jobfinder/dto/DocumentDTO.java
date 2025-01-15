package com.ilisi.jobfinder.dto;

import com.ilisi.jobfinder.Enum.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDTO {
    private byte[] data;
    private DocumentType fileType;
    private MediaType contentType;
}
