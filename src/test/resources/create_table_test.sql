CREATE TABLE IF NOT EXISTS Users
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    login    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL
);

CREATE TABLE IF NOT EXISTS Locations
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR NOT NULL,
    user_id   INT     NOT NULL REFERENCES Users (id),
    latitude  DECIMAL NOT NULL,
    longitude DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS Sessions
(
    id         VARCHAR PRIMARY KEY,
    user_id    INT       NOT NULL REFERENCES Users (id),
    expires_at TIMESTAMP NOT NULL
);