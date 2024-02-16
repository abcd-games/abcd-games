package com.github.abcdgames.backend.games.battleships.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBattleshipRequest {
    private BattleshipField[][] boardPlayer1;
}
