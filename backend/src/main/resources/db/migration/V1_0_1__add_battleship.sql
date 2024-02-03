CREATE TYPE BATTLESHIP_FIELD
AS ENUM (
    'EMPTY',
    'SHIP',
    'MISS',
    'HIT'
    );

CREATE TABLE IF NOT EXISTS battleship_board
(
    id     VARCHAR(200) not null,
    fields BATTLESHIP_FIELD[][],
    primary key (id)
);

CREATE TABLE IF NOT EXISTS battleship
(
    id                         VARCHAR(200) not null,
    required_players           INT,
    max_players                INT,
    board_player1_id           VARCHAR(200) REFERENCES battleship_board,
    board_player2_id           VARCHAR(200) REFERENCES battleship_board,
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
