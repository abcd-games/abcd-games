package com.github.abcdgames.backend.games.battleships.model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games/battleships")
@RequiredArgsConstructor
public class BattleshipController {


    private final BattleshipRepository battleshipRepository;

    @GetMapping
    List<Battleship> getAllBattleships() {
        return battleshipRepository.findAll();
    }

    @PostMapping
    Battleship createBattleship(@RequestBody Battleship battleship) {
        return battleshipRepository.save(battleship);
    }
}
