package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.games.battleships.model.Battleship;
import lombok.RequiredArgsConstructor;
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
    Battleship createBattleship(@RequestBody Battleship battleship) {
        return battleShipService.save(battleship);
    }
}
