package com.github.abcdgames.backend.player;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DirtiesContext
    @DisplayName("GET: Should return correct player by id.")
    @WithMockUser(roles = "ADMIN")
    void getPlayerByIdReturnsCorrectPlayer() throws Exception {
        Player playerToSave = Player.builder()
                .id("1")
                .displayName("John Doe")
                .build();
        Player expectedPlayer = playerRepository.save(playerToSave);

        mockMvc.perform(get("/api/players/" + expectedPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedPlayer.getId()))
                .andExpect(content().json("""
                        {
                            "displayName": "John Doe"
                        }
                        """));
    }

    @Test
    @DisplayName("GET: Should throw exception when player does not exist.")
    @WithMockUser(roles = "ADMIN")
    void getPlayerByIdThrowsExceptionWhenPlayerDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/players/2"))
                .andExpect(status().isNotFound());
    }
}
