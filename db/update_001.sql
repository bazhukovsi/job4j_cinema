CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR NOT NULL,
                       email VARCHAR NOT NULL UNIQUE,
                       password VARCHAR(20),
                       phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE movies (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(50),
                          time TIME
);

CREATE TABLE ticket (
                        id SERIAL PRIMARY KEY,
                        movie_id INT NOT NULL REFERENCES movies(id),
                        pos_row INT NOT NULL,
                        cell INT NOT NULL,
                        user_id INT NOT NULL REFERENCES users(id),
                        UNIQUE (movie_id, pos_row, cell)
);