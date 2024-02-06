package com.github.abcdgames.backend.games.battleships;

import com.github.abcdgames.backend.games.battleships.model.Battleship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleshipRepository extends JpaRepository<Battleship, String> {
}
