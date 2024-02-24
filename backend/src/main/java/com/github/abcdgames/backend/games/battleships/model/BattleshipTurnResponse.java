package com.github.abcdgames.backend.games.battleships.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BattleshipTurnResponse {

    private BattleshipTurnRequest battleshipTurnRequest;
    private BattleshipField result;

}
