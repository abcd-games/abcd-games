package com.github.abcdgames.backend.games.battleships.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class BattleshipConfig {

    public static final BattleshipConfig defaultConfig = defaultBattleShipConfig();

    private int boardSize;
    private int requiredPlayers;
    private int maxPlayers;
    private List<BattleshipShip> availableShipsPerPlayer;

    private BattleshipConfig() {

    }

    private static BattleshipConfig defaultBattleShipConfig() {
        return BattleshipConfig.builder()
                .boardSize(10)
                .maxPlayers(2)
                .requiredPlayers(1)
                .availableShipsPerPlayer(List.of(
                        BattleshipShip.CARRIER,
                        BattleshipShip.BATTLESHIP,
                        BattleshipShip.BATTLESHIP,
                        BattleshipShip.CRUISER,
                        BattleshipShip.CRUISER,
                        BattleshipShip.CRUISER,
                        BattleshipShip.DESTROYER,
                        BattleshipShip.DESTROYER,
                        BattleshipShip.DESTROYER,
                        BattleshipShip.DESTROYER))
                .build();
    }
}
