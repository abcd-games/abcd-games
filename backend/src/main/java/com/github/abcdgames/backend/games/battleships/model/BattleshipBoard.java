package com.github.abcdgames.backend.games.battleships.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "battleship_board")
public class BattleshipBoard {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Type(value = EnumArrayType.class)
    @Column(
            name = "fields",
            columnDefinition = "BATTLESHIP_FIELD[][]"
    )
    private BattleshipField[][] fields;
}
