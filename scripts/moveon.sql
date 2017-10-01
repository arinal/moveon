DROP DATABASE IF EXISTS Moveon;
CREATE DATABASE Moveon;
USE Moveon;

DROP TABLE IF EXISTS Movies;
CREATE TABLE Movies(
    imdb_id char(9) PRIMARY KEY,
    title varchar(100),
    director varchar(100)
);

DROP TABLE IF EXISTS Sessions;
CREATE TABLE Sessions(
    screen_id char(4) PRIMARY KEY,
    imdb_id char(9),
    initial_seats INT,
    reserved_seats INT
);

INSERT INTO Movies (imdb_id, title, director) VALUES ('tt0113497', 'Jumanji', 'Joe Johnston');
INSERT INTO Movies (imdb_id, title, director) VALUES ('tt0059742', 'The Sound of Music', 'Robert Wise');
INSERT INTO Movies (imdb_id, title, director) VALUES ('tt0110200', 'Fist of Legend', 'Gordon Chan');
