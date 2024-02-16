package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.appuser.AppUser;
import com.github.abcdgames.backend.games.battleships.model.Battleship;
import com.github.abcdgames.backend.games.battleships.model.BattleshipTurnRequest;
import com.github.abcdgames.backend.games.battleships.model.CreateBattleshipRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games/battleships")
@RequiredArgsConstructor
public class BattleshipController {


    private final BattleShipService battleShipService;

    @GetMapping
    List<Battleship> getAllBattleships() {
        return battleShipService.findAll();
    }

    @GetMapping("/{id}")
    Battleship getBattleshipById(@PathVariable String id) {
        return battleShipService.findById(id);
    }

    @PostMapping
    Battleship createBattleship(@AuthenticationPrincipal AppUser user, @RequestBody CreateBattleshipRequest battleshipRequest) {
        return battleShipService.createGame(battleshipRequest, user);
    }

    @PostMapping("/{id}/turn")
    Battleship makeTurn(@AuthenticationPrincipal AppUser user,
                        @RequestBody BattleshipTurnRequest battleshipTurnRequest,
                        @PathVariable String id) {
        return battleShipService.makeTurn(battleshipTurnRequest, id, user);
    }
}
