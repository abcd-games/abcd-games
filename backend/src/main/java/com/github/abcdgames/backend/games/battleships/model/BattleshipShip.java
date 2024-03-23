package com.github.abcdgames.backend.games.battleships.model;

import lombok.Getter;

@Getter
public enum BattleshipShip {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    DESTROYER(2),
    ;

    private final int length;

    BattleshipShip(int length) {
        this.length = length;
    }
}
