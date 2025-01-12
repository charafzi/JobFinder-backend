package com.ilisi.jobfinder.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Configuration du serveur Socket.IO pour la communication en temps réel.
 * Cette classe gère la configuration et le cycle de vie du serveur Socket.IO.
 */
@CrossOrigin
@Component
public class SocketIOConfig {
    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);
    
    /**
     * Host du serveur Socket.IO, injecté depuis la configuration
     */
    @Value("${socket.host}")
    private String SOCKETHOST;

    /**
     * Port du serveur Socket.IO, injecté depuis la configuration
     */
    @Value("${socket.port}")
    private int SOCKETPORT;

    /**
     * Instance du serveur Socket.IO
     */
    private SocketIOServer server;

    /**
     * Configure et démarre le serveur Socket.IO.
     * Initialise les écouteurs pour les événements de connexion et déconnexion.
     * 
     * @return SocketIOServer configuré et démarré
     */
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(SOCKETHOST);
        config.setPort(SOCKETPORT);

        // Configuration de Jackson pour permettre la sérialisation correcte des types de date/heure Java 8
        config.setJsonSupport(new JacksonJsonSupport(new JavaTimeModule()));

        server = new SocketIOServer(config);
        server.start();

        // Écouteur pour les connexions de clients
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                logger.info("New user connected with socket " + client.getSessionId());
            }
        });

        // Écouteur pour les déconnexions de clients
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                client.getNamespace().getAllClients().stream().forEach(data-> {
                    logger.info("User disconnected "+data.getSessionId().toString());
                });
            }
        });
        return server;
    }

    /**
     * Méthode appelée lors de l'arrêt de l'application.
     * Assure l'arrêt propre du serveur Socket.IO.
     */
    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }
}
