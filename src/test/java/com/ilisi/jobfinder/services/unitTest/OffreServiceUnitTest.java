package com.ilisi.jobfinder.services.unitTest;

import com.ilisi.jobfinder.repository.OffreEmploiRepository;
import com.ilisi.jobfinder.repository.UserRepository;
import com.ilisi.jobfinder.service.OffreEmploiService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OffreServiceUnitTest {
    @Mock
    private OffreEmploiRepository offremploiRepository;
    @Mock
    private UserRepository userRepository;

    private OffreEmploiService offreEmploiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offreEmploiService = new OffreEmploiService(offremploiRepository,userRepository);
    }

}
