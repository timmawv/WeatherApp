CREATE TABLE IF NOT EXISTS Users
(
    id       INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    login    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL
);

CREATE TABLE IF NOT EXISTS Locations
(
    id        INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    name      VARCHAR NOT NULL,
    user_id   INT     NOT NULL REFERENCES Users (id),
    latitude  DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL,
    UNIQUE (user_id, latitude, longitude)
);

CREATE TABLE IF NOT EXISTS Sessions
(
    id         VARCHAR,
    user_id    INT       NOT NULL REFERENCES Users (id),
    expires_at timestamp NOT NULL
);

INSERT INTO Users(login, password)
VALUES ('timur', '$2a$10$M55sGHCwyjyd6AuGcp0HKuu.LOjyA9kAIaPGIXy7mUQw.ByskwCjC'),
       ('dima', '$2a$10$M55sGHCwyjyd6AuGcp0HKuu.LOjyA9kAIaPGIXy7mUQw.ByskwCjC');