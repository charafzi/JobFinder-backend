package com.ilisi.jobfinder.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchRequestDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.OffreSearchResponseDTO;
import com.ilisi.jobfinder.dto.OffreEmploi.PageResponse;
import com.ilisi.jobfinder.mapper.OffreMapper;
import com.ilisi.jobfinder.model.OffreEmploi;
import com.ilisi.jobfinder.service.OffreEmploiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/offre")
public class OffreController {
    private static final Logger log = LoggerFactory.getLogger(OffreController.class);
    
    @Autowired
    private final OffreEmploiService offreEmploiService;
    @Autowired
    private SocketIOServer socketServer;

    private static final String OFFRE_CREATED_EVENT = "offre_created";
    private static final String OFFRES_ROOM = "offres";

    public OffreController(OffreEmploiService offreEmploiService, SocketIOServer socketServer){
        this.offreEmploiService = offreEmploiService;
        this.socketServer=socketServer;

        // Enregistrement des écouteurs d'événements Socket.IO
        this.socketServer.addConnectListener(onUserConnectWithSocket);     // Écouteur de connexion
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket); // Écouteur de déconnexion
    }

    @PostMapping
    public ResponseEntity<Void> createOffre(@Valid @RequestBody OffreDTO offreDTO) {
        try {
            OffreEmploi offre = OffreMapper.toEntity(offreDTO);
            offreEmploiService.create_Offre(offre);

            // Diffusion de la nouvelle offre aux clients dans la room 'offres'
            OffreDTO createdOffre = OffreMapper.toDto(offre);
            socketServer.getRoomOperations(OFFRES_ROOM).sendEvent(OFFRE_CREATED_EVENT, createdOffre);
            log.warn("Offre created and broadcasted to room: " + OFFRES_ROOM);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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


    /******************* Méthodes Socket.IO ********************/

    /// Gestionnaire d'événement de connexion d'un client
    public ConnectListener onUserConnectWithSocket = (client) -> {
        // Ajout automatique du client à la room des offres lors de la connexion
        client.joinRoom(OFFRES_ROOM);
        log.info("Client {} connected and joined offers room", client.getSessionId());
    };

    /// Gestionnaire d'événement de déconnexion d'un client
    public DisconnectListener onUserDisconnectWithSocket = (client) -> {
        // Retrait du client de la room des offres lors de la déconnexion
        client.leaveRoom(OFFRES_ROOM);
        log.info("Client {} disconnected and left offers room", client.getSessionId());
    };
}
