package com.ilisi.jobfinder.controller;

import com.ilisi.jobfinder.Enum.CandidatureStatus;
import com.ilisi.jobfinder.model.*;
import com.ilisi.jobfinder.repository.CandidatureRepository;
import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private UserRepository userRepository;
    private OffreEmploiRepository emploiRepository;
    private CandidatureRepository candidatureRepository;

    @GetMapping
    public ResponseEntity<?> protectedEndPoint(){
        return ResponseEntity.ok("Protetced enpoint ");
    }


}
