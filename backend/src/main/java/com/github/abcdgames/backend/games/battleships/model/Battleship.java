package com.github.abcdgames.backend.games.battleships.model;

import com.github.abcdgames.backend.player.Player;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.repository.cdi.Eager;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "battleship")
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

    @OneToOne(cascade = CascadeType.PERSIST)
    private BattleshipBoard boardPlayer1;

    @OneToOne(cascade = CascadeType.PERSIST)
    private BattleshipBoard boardPlayer2;

    @Enumerated
    @Column(name = "available_ships_per_player")
    private List<BattleshipShips> availableShipsPerPlayer;

    @ManyToOne
    private Player currentTurn;

    @ManyToOne
    private Player winner;
}
