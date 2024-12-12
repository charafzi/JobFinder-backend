package com.ilisi.jobfinder.model;

import com.ilisi.jobfinder.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name ="Utilisateur")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String photoProfile;
    private String telephone;
    private String googleId;
    @Enumerated(EnumType.STRING)
    private Role role;
}
