package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.Battleship;
import com.github.abcdgames.backend.games.battleships.model.BattleshipConfig;
import com.github.abcdgames.backend.games.battleships.model.CreateBattleshipRequest;
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
}
