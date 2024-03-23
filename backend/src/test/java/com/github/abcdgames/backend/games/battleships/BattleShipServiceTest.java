package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.*;
import com.github.abcdgames.backend.player.Player;
import com.github.abcdgames.backend.player.PlayerService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleShipServiceTest {

    BattleshipRepository battleshipRepository = mock(BattleshipRepository.class);
    PlayerService playerService = mock(PlayerService.class);
    BattleShipService battleShipService = new BattleShipService(battleshipRepository, playerService, 1L);

    @Test
    void findAll() {
        //GIVEN
        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = new Player("0", "BOT");

        BattleshipField[][] board1 = new BattleshipField[10][10];
        BattleshipField[][] board0 = new BattleshipField[10][10];

        Battleship battleship1 = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .winner(p1)
                .requiredPlayers(1)
                .maxPlayers(2)
                .playerBoards(Map.of(p1, board1, p0, board0))
                .build();

        Battleship battleship2 = Battleship.builder()
                .id("2")
                .currentTurn(p0)
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .requiredPlayers(1)
                .maxPlayers(2)
                .playerBoards(Map.of(p1, board1, p0, board0))
                .build();

        when(battleshipRepository.findAll()).thenReturn(List.of(battleship1, battleship2));

        //WHEN

        List<Battleship> actual = battleShipService.findAll();

        //THEN
        verify(battleshipRepository).findAll();
        assertEquals(2, actual.size());
        assertEquals(battleship1, actual.get(0));
        assertEquals(battleship2, actual.get(1));
    }

    @Test
    void createGame() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

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

        BattleshipField[][] boardP0 = new BattleshipField[][]{
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY}
        };

        CreateBattleshipRequest createBattleshipRequest = CreateBattleshipRequest.builder()
                .board(boardP1)
                .build();

        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);
        when(battleshipRepository.save(any(Battleship.class))).thenAnswer(invocation -> {
            Battleship argument = invocation.getArgument(0);
            argument.setId("1");
            return argument;
        });

        //WHEN

        Battleship actual = battleShipService.createGame(createBattleshipRequest, user);

        //THEN

        verify(playerService).getPlayerById(String.valueOf(user.getId()));
        verify(battleshipRepository).save(any(Battleship.class));

        assertEquals("1", actual.getId());
        assertEquals(1, actual.getRequiredPlayers());
        assertEquals(2, actual.getMaxPlayers());
        assertEquals(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer(), actual.getAvailableShipsPerPlayer());
        assertEquals(p1, actual.getCurrentTurn());
        assertNull(actual.getWinner());

        assertArrayEquals(boardP1, actual.getPlayerBoards().get(p1));
        assertArrayEquals(boardP0, actual.getPlayerBoards().get(p0));
    }

    @Test
    void findById() {
        //GIVEN
        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = new Player("0", "BOT");

        BattleshipField[][] board1 = new BattleshipField[][]{
                {BattleshipField.MISS, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.HIT}
        };

        BattleshipField[][] board2 = new BattleshipField[][]{
                {BattleshipField.SHIP, BattleshipField.HIT},
                {BattleshipField.MISS, BattleshipField.EMPTY}
        };

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .maxPlayers(BattleshipConfig.defaultConfig.getMaxPlayers())
                .requiredPlayers(BattleshipConfig.defaultConfig.getRequiredPlayers())
                .winner(null)
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .playerBoards(Map.of(p1, board1, p0, board2))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));

        //WHEN

        Battleship actual = battleShipService.findById("1");

        //THEN

        verify(battleshipRepository).findById("1");
        assertEquals(battleship, actual);
    }

    @Test
    void findById_whenInvalidId_expectException() {
        //GIVEN
        when(battleshipRepository.findById("1")).thenReturn(Optional.empty());

        //WHEN
        //THEN
        assertThrows(NoSuchElementException.class, () -> battleShipService.findById("1"));
    }

    @Test
    void makeTurn_whenCurrentTurnIsPlayerAndTargetIsShip_shouldHit() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

        BattleshipField[][] boardP1 = new BattleshipField[10][10];
        BattleshipField[][] boardP0 = new BattleshipField[10][10];
        boardP0[0][0] = BattleshipField.SHIP;

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p0, boardP0))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));
        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);

        BattleshipTurnRequest turnRequest = BattleshipTurnRequest.builder()
                .targetPlayerId(p0.getId())
                .x(0)
                .y(0)
                .build();

        //WHEN
        List<BattleshipTurnResponse> actual = battleShipService.makeTurn(turnRequest, "1", user);

        //THEN
        assertEquals(1, actual.size());
        assertEquals(BattleshipField.HIT, actual.get(0).getResult());
    }

    @Test
    void makeTurn_whenCurrentTurnIsPlayerAndTargetIsNotShip_shouldMissAndNpcMiss() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

        BattleshipField[][] boardP1= new BattleshipField[][]{
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY}
        };
        BattleshipField[][] boardP0 = new BattleshipField[][]{
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY}
        };

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p0, boardP0))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));
        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);

        BattleshipTurnRequest turnRequest = BattleshipTurnRequest.builder()
                .targetPlayerId(p0.getId())
                .x(0)
                .y(0)
                .build();

        //WHEN
        List<BattleshipTurnResponse> actual = battleShipService.makeTurn(turnRequest, "1", user);

        //THEN
        assertEquals(2, actual.size());
        assertEquals(BattleshipField.MISS, actual.get(0).getResult());
        assertEquals(BattleshipField.MISS, actual.get(1).getResult());
    }

    @Test
    void makeTurn_whenCurrentTurnIsPlayerAndTargetIsNotShip_shouldMissAndNPCHit() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

        BattleshipField[][] boardP1= new BattleshipField[][]{
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY}
        };
        BattleshipField[][] boardP0 = new BattleshipField[][]{
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY},
                {BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.SHIP, BattleshipField.EMPTY, BattleshipField.EMPTY, BattleshipField.EMPTY}
        };

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p0, boardP0))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));
        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);

        BattleshipTurnRequest turnRequest = BattleshipTurnRequest.builder()
                .targetPlayerId(p0.getId())
                .x(0)
                .y(0)
                .build();

        //WHEN
        List<BattleshipTurnResponse> actual = battleShipService.makeTurn(turnRequest, "1", user);

        //THEN
        assertEquals(3, actual.size());
        assertEquals(BattleshipField.MISS, actual.get(0).getResult());
        assertEquals(BattleshipField.HIT, actual.get(1).getResult());
        assertEquals(BattleshipField.MISS, actual.get(2).getResult());
    }

    @Test
    void makeTurn_whenCurrentTurnIsNotPlayer_shouldThrowException() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

        BattleshipField[][] boardP1 = new BattleshipField[10][10];
        BattleshipField[][] boardP0 = new BattleshipField[10][10];

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p0)
                .playerBoards(Map.of(p1, boardP1, p0, boardP0))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));
        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);

        BattleshipTurnRequest turnRequest = BattleshipTurnRequest.builder()
                .targetPlayerId(p0.getId())
                .x(0)
                .y(0)
                .build();

        //WHEN
        //THEN
        assertThrows(IllegalArgumentException.class, () -> battleShipService.makeTurn(turnRequest, "1", user));
    }

    @Test
    void makeTurn_whenTargetFieldIsAlreadyHit_shouldThrowException() {
        //GIVEN
        AppUser user = AppUser.builder()
                .id(1L)
                .build();

        Player p1 = Player.builder()
                .id("1")
                .displayName("Player1")
                .build();

        Player p0 = Player.builder()
                .id("0")
                .displayName("BOT")
                .build();

        BattleshipField[][] boardP1 = new BattleshipField[10][10];
        BattleshipField[][] boardP0 = new BattleshipField[10][10];
        boardP0[0][0] = BattleshipField.HIT;

        Battleship battleship = Battleship.builder()
                .id("1")
                .currentTurn(p1)
                .playerBoards(Map.of(p1, boardP1, p0, boardP0))
                .build();

        when(battleshipRepository.findById("1")).thenReturn(Optional.of(battleship));
        when(playerService.getPlayerById(String.valueOf(user.getId()))).thenReturn(p1);

        BattleshipTurnRequest turnRequest = BattleshipTurnRequest.builder()
                .targetPlayerId(p0.getId())
                .x(0)
                .y(0)
                .build();

        //WHEN
        //THEN
        assertThrows(IllegalArgumentException.class, () -> battleShipService.makeTurn(turnRequest, "1", user));
    }
}
