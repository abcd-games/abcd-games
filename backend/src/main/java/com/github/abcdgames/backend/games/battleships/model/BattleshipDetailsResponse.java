package com.github.abcdgames.backend.games.battleships.model;

import com.github.abcdgames.backend.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleshipDetailsResponse {
    private String id;
    private List<Player> players;
    private int requiredPlayers;
    private int maxPlayers;
    private Map<String, BattleshipField[][]> boards;
    private List<BattleshipShip> availableShipsPerPlayer;
    private Player currentTurn;
    private Player winner;
}
