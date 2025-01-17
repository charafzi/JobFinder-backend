package com.ilisi.jobfinder.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilisi.jobfinder.Enum.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    @Enumerated(EnumType.STRING)
    private DocumentType fileType;
    private LocalDateTime uploadDate;
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
}
