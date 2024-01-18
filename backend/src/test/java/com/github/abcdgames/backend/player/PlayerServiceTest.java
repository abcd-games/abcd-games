package com.github.abcdgames.backend.player;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerServiceTest {

    private final PlayerRepository playerRepository = mock(PlayerRepository.class);
    private final PlayerService playerService = new PlayerService(playerRepository);

    @Test
    void getPlayerByIdReturnsCorrectPlayer() {
        //GIVEN
        Player expectedPlayer = new Player("1", "John Doe");
        when(playerRepository.findById("1")).thenReturn(Optional.of(expectedPlayer));

        //WHEN
        Player actualPlayer = playerService.getPlayerById("1");

        //THEN
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void getPlayerByIdThrowsExceptionWhenPlayerDoesNotExist() {
        //GIVEN
        when(playerRepository.findById("2")).thenReturn(Optional.empty());

        //WHEN and THEN
        assertThrows(NoSuchElementException.class, () -> playerService.getPlayerById("2"));
    }
}
