package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.*;
import com.github.abcdgames.backend.player.Player;
import com.github.abcdgames.backend.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BattleShipService {

    private final BattleshipRepository battleshipRepository;
    private final PlayerService playerService;

    public List<Battleship> findAll() {
        return battleshipRepository.findAll();
    }

    public Battleship createGame(CreateBattleshipRequest battleshipRequest, AppUser user) {
        Player player = playerService.getPlayerById(String.valueOf(user.getId()));

        Battleship createdBattleship = Battleship.builder()
                .availableShipsPerPlayer(BattleshipConfig.defaultConfig.getAvailableShipsPerPlayer())
                .maxPlayers(BattleshipConfig.defaultConfig.getMaxPlayers())
                .requiredPlayers(BattleshipConfig.defaultConfig.getRequiredPlayers())
                .playerBoards(Map.of(player, battleshipRequest.getBoard(), new Player("0", "BOT"), battleshipRequest.getBoard()))
                .currentTurn(player)
                .build();

        return battleshipRepository.save(createdBattleship);
    }

    public Battleship findById(String id) {
        return battleshipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Battleship with id: " + id + " not found."));
    }

    public Battleship makeTurn(BattleshipTurnRequest battleshipTurnRequest, String id, AppUser user) {
        Battleship battleship = findById(id);
        Player player = playerService.getPlayerById(String.valueOf(user.getId()));

        if (battleship.getCurrentTurn().getId().equals(player.getId())) {

            BattleshipField[][] board = battleship.getPlayerBoards().entrySet().stream()
                    .filter(playerEntry -> playerEntry.getKey().getId().equals(battleshipTurnRequest.targetPlayerId()))
                    .findAny()
                    .orElseThrow()
                    .getValue();

            BattleshipField target = board[battleshipTurnRequest.y()][battleshipTurnRequest.x()];

            if (target == BattleshipField.HIT || target == BattleshipField.MISS) {
                throw new IllegalArgumentException("Field already targeted.");
            }

            if (target == BattleshipField.SHIP) {
                board[battleshipTurnRequest.y()][battleshipTurnRequest.x()] = BattleshipField.HIT;
            } else {
                board[battleshipTurnRequest.y()][battleshipTurnRequest.x()] = BattleshipField.MISS;
            }

            return battleshipRepository.save(battleship);
        } else {
            throw new IllegalArgumentException("It's not your turn.");
        }
    }
}
