package com.github.abcdgames.backend.games.battleships.model;

import com.github.abcdgames.backend.player.Player;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "battleship")
@With
public class Battleship {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "battleship_players",
            joinColumns = @JoinColumn(name = "battleship_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players;

    @Column(name = "required_players")
    private int requiredPlayers;
    @Column(name = "max_players")
    private int maxPlayers;

    @Type(value = EnumArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = "sql_array_type",
                    value = "battleship_field"
            ))
    @Column(
            name = "board_player1",
            columnDefinition = "battleship_field[][]"
    )
    private BattleshipField[][] boardPlayer1;

    @Type(value = EnumArrayType.class,
            parameters = @org.hibernate.annotations.Parameter(
                    name = "sql_array_type",
                    value = "battleship_field"
            ))
    @Column(
            name = "board_player2",
            columnDefinition = "battleship_field[][]"
    )
    private BattleshipField[][] boardPlayer2;

    @Enumerated
    @Column(name = "available_ships_per_player")
    private List<BattleshipShip> availableShipsPerPlayer;

    @ManyToOne
    private Player currentTurn;

    @ManyToOne
    private Player winner;
}
