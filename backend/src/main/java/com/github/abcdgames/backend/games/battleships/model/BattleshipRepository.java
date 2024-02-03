package com.github.abcdgames.backend.games.battleships.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleshipRepository extends JpaRepository<Battleship, String> {
}
