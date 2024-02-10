package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.games.battleships.model.Battleship;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    public Battleship findById(String id) {
        return battleshipRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Battleship with id: " +id + " not found."));
    }
}
