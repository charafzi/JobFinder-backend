package com.ilisi.jobfinder.controller;


import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.mapper.OffreMapper;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.service.OffreEmploiService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/api/offre")
public class OffreController {
    private final OffreEmploiService offreEmploiService;

    @PostMapping
    public ResponseEntity<Void> createOffre(@Valid @RequestBody OffreDTO offreDTO) {
        try {
            OffreEmploi offre = OffreMapper.toEntity(offreDTO);
            offreEmploiService.create_Offre(offre);
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 Bad Request
        }
    }

    @GetMapping
    public ResponseEntity<List<OffreDTO>> getAllOffers() {
        try {
            List<OffreDTO> offres = offreEmploiService.getAllOffres()
                    .stream()
                    .map(OffreMapper::toDto)
                    .toList();
            return ResponseEntity.ok(offres); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 500 Internal Server Error
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getOffresNearby(@RequestParam double latitude,
                                             @RequestParam double longitude,
                                             @RequestParam double radius){
        try {
            List<OffreDTO> offreDTOS =  this.offreEmploiService.getOffresInRadius(latitude, longitude, radius);
            return ResponseEntity.ok(offreDTOS);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body("Error while retrieving offres nearby = ["+latitude+","+longitude+"]("+radius+").");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreDTO> getOffreById(@PathVariable int id) {
        try {
            return offreEmploiService.getOffreById(id)
                    .map(OffreMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreDTO> updateOffre(@PathVariable int id, @RequestBody OffreDTO updatedOffreDTO) {
        try {
            OffreEmploi updatedOffre = OffreMapper.toEntity(updatedOffreDTO);
            OffreEmploi savedOffre = offreEmploiService.updateOffre(id, updatedOffre);
            return ResponseEntity.ok(OffreMapper.toDto(savedOffre)); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffre(@PathVariable int id) {
        try {
            offreEmploiService.deleteOffre(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<OffreSearchResponseDTO>> searchOffres(@RequestBody OffreSearchRequestDTO searchRequestDTO) {
        try {
            Page<OffreSearchResponseDTO> result = this.offreEmploiService.searchOffres(searchRequestDTO);
            return ResponseEntity.ok(PageResponse.of(result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
