CREATE TYPE battleship_field
AS ENUM (
    'EMPTY',
    'SHIP',
    'MISS',
    'HIT'
    );

CREATE TABLE IF NOT EXISTS battleship
(
    id                         VARCHAR(200) not null,
    required_players           INT,
    max_players                INT,
    board_player1              battleship_field[][],
    board_player2              battleship_field[][],
    available_ships_per_player INT[],
    current_turn_id            VARCHAR(200) REFERENCES player,
    winner_id                  VARCHAR(200) REFERENCES player,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS battleship_players
(
    battleship_id VARCHAR(200) REFERENCES battleship,
    player_id     VARCHAR(200) REFERENCES player,
    primary key (battleship_id, player_id)
);
