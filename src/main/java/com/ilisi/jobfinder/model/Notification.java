package com.ilisi.jobfinder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
 private LocalDateTime dateEnvoi;
 private boolean vue;
 @ManyToOne
 @JoinColumn(name = "user_id")
 private User user;
}
