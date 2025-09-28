CREATE TABLE player
(
    minecraft_id       UUID PRIMARY KEY,
    minecraft_username VARCHAR(16)                            NOT NULL, -- Not validated as unique as it's only updated on login
    discord_id         BIGINT UNIQUE CHECK ( discord_id > 0 ) NOT NULL,

    last_login         TIMESTAMP                              NOT NULL
);

CREATE TABLE link_request
(
    id                 VARCHAR(6) PRIMARY KEY,
    minecraft_id       UUID        NOT NULL UNIQUE,
    minecraft_username VARCHAR(16) NOT NULL,
    created_at         TIMESTAMP   NOT NULL
)