CREATE TABLE IF NOT EXISTS player
(
    id           VARCHAR(200) not null,
    display_name varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS app_user
(
    app_user_id BIGSERIAL NOT NULL,
    username    VARCHAR(20) UNIQUE NOT NULL,
    password    VARCHAR(100) NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    role        VARCHAR(255) NOT NULL,
    PRIMARY KEY (app_user_id)
);
