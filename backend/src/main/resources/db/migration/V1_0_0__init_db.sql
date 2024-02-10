CREATE TABLE IF NOT EXISTS player
(
    id           VARCHAR(200) not null,
    display_name varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS app_user
(
    app_user_id BIGSERIAL NOT NULL,
    username
    VARCHAR
(
    20
),
    email VARCHAR
(
    255
),
    avatar_url VARCHAR
(
    255
),
    role VARCHAR
(
    255
),
    PRIMARY KEY (app_user_id)
);
