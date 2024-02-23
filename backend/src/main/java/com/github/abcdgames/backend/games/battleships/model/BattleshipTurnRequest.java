package com.github.abcdgames.backend.games.battleships.model;

public record BattleshipTurnRequest(
        String targetPlayerId,
        int x,
        int y
) {
}
