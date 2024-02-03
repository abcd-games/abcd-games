package com.github.abcdgames.backend.games.battleships.model;

public enum BattleshipShips {
    CARRIER(5),
    BATTLESHIP(4),
    CRUISER(3),
    DESTROYER(2),
    ;

    private final int length;

    BattleshipShips(int length) {
        this.length = length;
    }
}
