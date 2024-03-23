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
    available_ships_per_player INT[],
    current_turn_id            VARCHAR(200) REFERENCES player,
    winner_id                  VARCHAR(200) REFERENCES player,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS battleship_player_boards
(
    battleship_id    VARCHAR(200) REFERENCES battleship,
    player_id        VARCHAR(200) REFERENCES player,
    board            battleship_field[][],
    primary key (battleship_id, player_id)
);
