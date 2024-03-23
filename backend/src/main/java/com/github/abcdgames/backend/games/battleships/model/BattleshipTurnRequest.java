package com.github.abcdgames.backend.games.battleships.model;

import lombok.Builder;

@Builder
public record BattleshipTurnRequest(
        String targetPlayerId,
        int x,
        int y
) {
}
