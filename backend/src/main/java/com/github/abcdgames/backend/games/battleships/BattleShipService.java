package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.*;
import com.github.abcdgames.backend.player.Player;
import com.github.abcdgames.backend.player.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .boardPlayer1(battleshipRequest.getBoardPlayer1())
                .boardPlayer2(BattleshipField.createEmptyBoard())
                .players(List.of(player))
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

            BattleshipField[][] board = battleship.getPlayers().indexOf(battleship.getCurrentTurn()) == 0
                    ? battleship.getBoardPlayer2()
                    : battleship.getBoardPlayer1();

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
