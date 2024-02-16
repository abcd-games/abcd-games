package com.github.abcdgames.backend.games.battleships.model;

public enum BattleshipField {
    EMPTY,
    SHIP,
    MISS,
    HIT;

    public static BattleshipField[][] createEmptyBoard() {
        return createEmptyBoard(BattleshipConfig.defaultConfig.getBoardSize());
    }

    private static BattleshipField[][] createEmptyBoard(int boardSize) {
        BattleshipField[][] board = new BattleshipField[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = EMPTY;
            }
        }
        return board;
    }
}
