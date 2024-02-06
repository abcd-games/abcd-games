package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.games.battleships.model.Battleship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleShipService {

    private final BattleshipRepository battleshipRepository;

    public List<Battleship> findAll() {
        return battleshipRepository.findAll();
    }

    public Battleship save(Battleship battleship) {
        return battleshipRepository.save(battleship);
    }
}
