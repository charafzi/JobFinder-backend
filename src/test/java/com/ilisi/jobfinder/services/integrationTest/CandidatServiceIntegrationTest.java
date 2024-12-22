package com.ilisi.jobfinder.services.integrationTest;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.model.Candidat;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class CandidatServiceIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("jobfinder_test")
            .withUsername("username")
            .withPassword("password")
            .withExposedPorts(5432);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = String.format("jdbc:postgresql://localhost:%d/%s",
                postgresContainer.getMappedPort(5432), postgresContainer.getDatabaseName());
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegisterCandidat() throws EmailAlreadyExists {
        Candidat candidat = new Candidat();
        candidat.setEmail("test@example.com");
        candidat.setTelephone("0645201020");
        candidat.setPassword("123456789");
        candidat.setRole(Role.CANDIDAT);

        User savedUser = userService.saveUser(candidat);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(candidat.getEmail());
        assertThat(savedUser.getTelephone()).isEqualTo(candidat.getTelephone());
        assertThat(savedUser.getPassword()).isEqualTo(candidat.getPassword());
        assertThat(savedUser.getRole()).isEqualTo(candidat.getRole());
    }
}
