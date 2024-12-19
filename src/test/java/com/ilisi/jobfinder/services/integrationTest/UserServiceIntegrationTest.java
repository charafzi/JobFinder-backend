package com.ilisi.jobfinder.services.integrationTest;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class UserServiceIntegrationTest {

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
    public void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setGoogleId("12345");

        User savedUser = userService.createUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setGoogleId("12345");
        userService.createUser(user);

        Optional<User> foundUser = userService.getUserByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testGetUserByGoogleId() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setGoogleId("12345");
        userService.createUser(user);

        Optional<User> foundUser = userService.getUserbyGoogleId("12345");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getGoogleId()).isEqualTo("12345");
    }
}
