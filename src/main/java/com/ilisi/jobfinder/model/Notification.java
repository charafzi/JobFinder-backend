package com.ilisi.jobfinder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 private String titre;
 private String contenu;
 @JdbcTypeCode(SqlTypes.JSON)
 @Column(columnDefinition = "jsonb")
 private Map<String,String> data;
 private LocalDateTime dateEnvoi;
 private boolean vue;
 @ManyToOne
 @JoinColumn(name = "user_id")
 private User user;
}
