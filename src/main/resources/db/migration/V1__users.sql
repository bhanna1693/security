CREATE TABLE IF NOT EXISTS users
(
    first_name VARCHAR(256)        NOT NULL,
    last_name  VARCHAR(256)        NOT NULL,
    email      VARCHAR(256) UNIQUE NOT NULL,
    password   VARCHAR(256)        NOT NULL,
    role       VARCHAR(50),
    id         SERIAL PRIMARY KEY
);
