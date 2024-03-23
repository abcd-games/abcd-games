package com.github.abcdgames.backend.games.battleships.model;

import com.github.abcdgames.backend.player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleshipListResponse {
    private String id;
    private List<Player> players;
    private int requiredPlayers;
    private int maxPlayers;
    private List<BattleshipShip> availableShipsPerPlayer;
    private Player currentTurn;
    private Player winner;
}
