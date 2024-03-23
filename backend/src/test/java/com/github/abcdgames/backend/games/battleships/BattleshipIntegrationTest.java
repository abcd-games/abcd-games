package com.github.abcdgames.backend.games.battleships;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.appuser.AppUserRepository;
import com.github.abcdgames.backend.appuser.AppUserRole;
import com.github.abcdgames.backend.games.battleships.model.Battleship;
import com.github.abcdgames.backend.games.battleships.model.BattleshipConfig;
import com.github.abcdgames.backend.games.battleships.model.BattleshipField;
import com.github.abcdgames.backend.player.Player;
import com.github.abcdgames.backend.player.PlayerRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BattleshipIntegrationTest {

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
    MockMvc mockMvc;

    @Autowired
    BattleshipRepository battleshipRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBattleships_withoutLogin_expectAllBattleshipWithoutAnyBoards() throws Exception {
        //GIVEN
        Player p1 = playerRepository.save(Player.builder()
                .id("1")
                .displayName("player1")
                .build());
        Player p2 = playerRepository.save(Player.builder()
                .id("2")
                .displayName("player2")
                .build());

        BattleshipField[][] boardP1 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};
        BattleshipField[][] boardP2 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};


        Battleship battleship = battleshipRepository.save(Battleship.builder()
                .id("1")
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p2, boardP2))
                .winner(p1)
                .maxPlayers(2)
                .requiredPlayers(1)
                .build());

        //WHEN

        mockMvc.perform(get("/api/games/battleships"))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            [
                               {  
                                "requiredPlayers": 1,
                                "maxPlayers": 2,
                                "availableShipsPerPlayer": ["CARRIER","BATTLESHIP","BATTLESHIP","CRUISER","CRUISER","CRUISER","DESTROYER","DESTROYER","DESTROYER","DESTROYER"],
                                "currentTurn":{"displayName":"player1"},
                                "winner": {"displayName":"player1"}
                                }
                            ]
                        """))
                .andExpect(jsonPath("$[0].id").value(battleship.getId()))
                .andExpect(jsonPath("$[0].players[*].id", containsInAnyOrder("1", "2")))
                .andExpect(jsonPath("$[0].players[*].displayName", containsInAnyOrder("player1", "player2")))
                .andExpect(jsonPath("$[0].winner.id").value(p1.getId()))
                .andExpect(jsonPath("$[0].currentTurn.id").value(p1.getId()))
                .andExpect(jsonPath("$[0].playerBoards").doesNotExist());
    }

    @Test
    void getBattleshipById_withLoginAsP1_expectBattleshipWithoutEnemyShips() throws Exception {
        //GIVEN
        AppUser user = appUserRepository.save(AppUser.builder()
                .email("ab@c.de")
                .username("user1")
                .password("Password123")
                .role(AppUserRole.USER)
                .build());

        Player p1 = playerRepository.save(Player.builder()
                .id(String.valueOf(user.getId()))
                .displayName("player1")
                .build());
        Player p2 = playerRepository.save(Player.builder()
                .id("2")
                .displayName("player2")
                .build());

        BattleshipField[][] boardP1 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};
        BattleshipField[][] boardP2 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};


        Battleship battleship = battleshipRepository.save(Battleship.builder()
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p2, boardP2))
                .winner(p1)
                .maxPlayers(2)
                .requiredPlayers(1)
                .build());

        //WHEN

        mockMvc.perform(get("/api/games/battleships/" + battleship.getId())
                        .with(authentication(new UsernamePasswordAuthenticationToken(user, null))))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                               {
                                "id": "%s",
                                "requiredPlayers": 1,
                                "maxPlayers": 2,
                                "availableShipsPerPlayer": ["CARRIER","BATTLESHIP","BATTLESHIP","CRUISER","CRUISER","CRUISER","DESTROYER","DESTROYER","DESTROYER","DESTROYER"],
                                "currentTurn":{
                                    "id":"%s",
                                    "displayName":"player1"
                                },
                                "winner": {
                                    "id":"%s",
                                    "displayName":"player1"
                                },
                                "boards": {
                                "%s": [["HIT", "SHIP"], ["EMPTY", "MISS"]],
                                "2":[["HIT","EMPTY"],["EMPTY","MISS"]]}
                                }
                        """.formatted(battleship.getId(), p1.getId(), p1.getId(), user.getId())))
                .andExpect(jsonPath("$.players[*].id", containsInAnyOrder("1", "2")))
                .andExpect(jsonPath("$.players[*].displayName", containsInAnyOrder("player1", "player2")))
                .andExpect(jsonPath("$.playerBoards").doesNotExist())
                .andDo(result -> {
                    assertFalse(result.getResponse().getContentAsString().matches(".*boards.*\"2\":.*SHIP.*]].*"));
                });
    }

    @Test
    void getBattleshipById_withoutLogin_expectBattleshipWithoutAnyShips() throws Exception {
        //GIVEN
        Player p1 = playerRepository.save(Player.builder()
                .id("1")
                .displayName("player1")
                .build());
        Player p2 = playerRepository.save(Player.builder()
                .id("2")
                .displayName("player2")
                .build());

        BattleshipField[][] boardP1 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};
        BattleshipField[][] boardP2 = new BattleshipField[][]{{BattleshipField.HIT, BattleshipField.SHIP}, {BattleshipField.EMPTY, BattleshipField.MISS}};


        Battleship battleship = battleshipRepository.save(Battleship.builder()
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p2, boardP2))
                .winner(p1)
                .maxPlayers(2)
                .requiredPlayers(1)
                .build());

        //WHEN

        mockMvc.perform(get("/api/games/battleships/" + battleship.getId()))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                               {
                                "id": "%s",
                                "requiredPlayers": 1,
                                "maxPlayers": 2,
                                "availableShipsPerPlayer": ["CARRIER","BATTLESHIP","BATTLESHIP","CRUISER","CRUISER","CRUISER","DESTROYER","DESTROYER","DESTROYER","DESTROYER"],
                                "currentTurn":{
                                    "id":"%s",
                                    "displayName":"player1"
                                },
                                "winner": {
                                    "id":"%s",
                                    "displayName":"player1"
                                },
                                "boards": {
                                "1": [["HIT", "EMPTY"], ["EMPTY", "MISS"]],
                                "2":[["HIT","EMPTY"],["EMPTY","MISS"]]}
                                }
                        """.formatted(battleship.getId(), p1.getId(), p1.getId())))
                .andExpect(jsonPath("$.players[*].id", containsInAnyOrder("1", "2")))
                .andExpect(jsonPath("$.players[*].displayName", containsInAnyOrder("player1", "player2")))
                .andExpect(jsonPath("$.playerBoards").doesNotExist())
                .andDo(result -> {
                    assertFalse(result.getResponse().getContentAsString().matches(".*boards.*SHIP.*]].*"));
                });
    }


    @Test
    void createBattleship_expectBattleship() throws Exception {
        //GIVEN
        AppUser user = appUserRepository.save(AppUser.builder()
                .email("ab@c.de")
                .username("user1")
                .password("Password123")
                .role(AppUserRole.USER)
                .build());

        Player p0 = playerRepository.save(Player.builder()
                .id("0")
                .displayName("BOT")
                .build());
        Player p1 = playerRepository.save(Player.builder()
                .id(String.valueOf(user.getId()))
                .displayName("player1")
                .build());


        //WHEN

        mockMvc.perform(post("/api/games/battleships")
                        .with(authentication(new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("USER")))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "board": [["SHIP","EMPTY"],["MISS","HIT"]]
                                }
                                """))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                               {
                                "requiredPlayers": 1,
                                "maxPlayers": 2,
                                "availableShipsPerPlayer": ["CARRIER","BATTLESHIP","BATTLESHIP","CRUISER","CRUISER","CRUISER","DESTROYER","DESTROYER","DESTROYER","DESTROYER"],
                                "currentTurn":{
                                    "id":"%s",
                                    "displayName":"player1"
                                },
                                "boards": {
                                    "%s": [["SHIP","EMPTY"],["MISS","HIT"]]
                                    }
                                }
                        """.formatted(p1.getId(), p1.getId())))
                .andExpect(jsonPath("$.players[*].id", containsInAnyOrder("0", p1.getId())))
                .andExpect(jsonPath("$.players[*].displayName", containsInAnyOrder("BOT", "player1")))
                .andExpect(jsonPath("$.playerBoards").doesNotExist())
                .andDo(result -> {
                    assertFalse(result.getResponse().getContentAsString().matches(".*boards.*BOT.*SHIP.*]].*"));
                });
    }


    @Test
    void postMakeTurn_withLoginAsP1_expectOwnAndEnemyTurn() throws Exception {
        //GIVEN
        AppUser user = appUserRepository.save(AppUser.builder()
                .email("ab@c.de")
                .username("user1")
                .password("Password123")
                .role(AppUserRole.USER)
                .build());

        Player p1 = playerRepository.save(Player.builder()
                .id(String.valueOf(user.getId()))
                .displayName("player1")
                .build());
        Player p2 = playerRepository.save(Player.builder()
                .id("0")
                .displayName("BOT")
                .build());

        BattleshipField[][] boardP1 = new BattleshipField[][]{
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
        };
        BattleshipField[][] boardP2 = new BattleshipField[][]{
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
                {BattleshipField.HIT, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY,},
        };

        Battleship battleship = battleshipRepository.save(Battleship.builder()
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p2, boardP2))
                .winner(p1)
                .maxPlayers(2)
                .requiredPlayers(1)
                .build());

        //WHEN

        mockMvc.perform(post("/api/games/battleships/" + battleship.getId() + "/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "x": 2,
                                    "y": 0,
                                    "targetPlayerId": "0"
                                }
                                """)
                        .with(authentication(new UsernamePasswordAuthenticationToken(user, null, List.of()))))

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                               [
                               {
                                   "battleshipTurnRequest": {
                                        "targetPlayerId": "0",
                                        "x": 2,
                                        "y": 0
                                    },
                                    "result": "MISS"
                               },
                               {
                                   "battleshipTurnRequest": {
                                        "targetPlayerId": "%s"
                                    }
                               }
                               ]
                        """.formatted(user.getId())))
                .andExpect(jsonPath("$[1]").exists());
    }
}
