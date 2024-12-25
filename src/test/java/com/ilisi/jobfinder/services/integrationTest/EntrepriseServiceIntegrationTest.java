package com.ilisi.jobfinder.services.integrationTest;

import com.ilisi.jobfinder.Enum.Role;
import com.ilisi.jobfinder.exceptions.EmailAlreadyExists;
import com.ilisi.jobfinder.model.Entreprise;
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
public class EntrepriseServiceIntegrationTest {
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
    public void testRegisterEntreprise() throws EmailAlreadyExists {
        Entreprise entreprise = new Entreprise();
        entreprise.setEmail("test@test.com");
        entreprise.setPassword("123456789");
        entreprise.setAdresse("adress1");
        entreprise.setTelephone("0620302030");
        entreprise.setRole(Role.ENTREPRISE);

        User savedUser = userService.saveUser(entreprise);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(entreprise.getEmail());
        assertThat(savedUser.getTelephone()).isEqualTo(entreprise.getTelephone());
        assertThat(savedUser.getPassword()).isEqualTo(entreprise.getPassword());
        assertThat(savedUser.getRole()).isEqualTo(entreprise.getRole());
    }
}
