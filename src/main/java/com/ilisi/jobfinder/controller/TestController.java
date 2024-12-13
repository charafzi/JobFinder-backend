package com.ilisi.jobfinder.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<?> protectedEndPoint(){
        return ResponseEntity.ok().body("This endpoint is protected !");
    }
}
