package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games/battleships")
@RequiredArgsConstructor
public class BattleshipController {


    private final BattleShipService battleShipService;

    @GetMapping
    List<BattleshipListResponse> getAllBattleships() {
        return battleShipService.findAll().stream()
                .map(battleship -> BattleshipListResponse.builder()
                        .id(battleship.getId())
                        .players(new ArrayList<>(battleship.getPlayerBoards().keySet()))
                        .requiredPlayers(battleship.getRequiredPlayers())
                        .maxPlayers(battleship.getMaxPlayers())
                        .availableShipsPerPlayer(battleship.getAvailableShipsPerPlayer())
                        .currentTurn(battleship.getCurrentTurn())
                        .winner(battleship.getWinner())
                        .build())
                .toList();
    }

    @GetMapping("/{id}")
    BattleshipDetailsResponse getBattleshipById(@AuthenticationPrincipal AppUser user, @PathVariable String id) {
        Battleship battleship = battleShipService.findById(id);

        return getBattleshipDetailsResponse(battleship, user);
    }

    @PostMapping
    BattleshipDetailsResponse createBattleship(@AuthenticationPrincipal AppUser user, @RequestBody CreateBattleshipRequest battleshipRequest) {
        Battleship createdBattleship = battleShipService.createGame(battleshipRequest, user);

        return getBattleshipDetailsResponse(createdBattleship, user);
    }

    @PostMapping("/{id}/turn")
    BattleshipDetailsResponse makeTurn(@AuthenticationPrincipal AppUser user,
                                       @RequestBody BattleshipTurnRequest battleshipTurnRequest,
                                       @PathVariable String id) {
        Battleship battleshipAfterTurn = battleShipService.makeTurn(battleshipTurnRequest, id, user);

        return getBattleshipDetailsResponse(battleshipAfterTurn, user);
    }

    private BattleshipDetailsResponse getBattleshipDetailsResponse(Battleship createdBattleship, AppUser user) {
        return BattleshipDetailsResponse.builder()
                .boards(createdBattleship.getPlayerBoards().entrySet()
                        .stream()
                        .map(entry -> Map.entry(entry.getKey().getId(), entry.getValue()))
                        .map(entry -> Map.entry(entry.getKey(), (user != null && entry.getKey().equals(user.getId().toString()))
                                ? entry.getValue()
                                : blurShips(entry.getValue())))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .id(createdBattleship.getId())
                .players(new ArrayList<>(createdBattleship.getPlayerBoards().keySet()))
                .requiredPlayers(createdBattleship.getRequiredPlayers())
                .maxPlayers(createdBattleship.getMaxPlayers())
                .availableShipsPerPlayer(createdBattleship.getAvailableShipsPerPlayer())
                .currentTurn(createdBattleship.getCurrentTurn())
                .winner(createdBattleship.getWinner())
                .build();
    }

    public BattleshipField[][] blurShips(BattleshipField[][] fields) {
        BattleshipField[][] blurredCopy = new BattleshipField[fields.length][fields[0].length];
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == BattleshipField.SHIP) {
                    blurredCopy[i][j] = BattleshipField.EMPTY;
                } else {
                    blurredCopy[i][j] = fields[i][j];
                }
            }
        }
        return blurredCopy;
    }
}
