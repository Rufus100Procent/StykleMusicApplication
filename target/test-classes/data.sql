CREATE TABLE IF NOT EXISTS song (
release_year integer not null,
id SERIAL not null,
album varchar(255),
artist_name varchar(255) not null,
file_path varchar(255) not null,
genre varchar(255),
song_name varchar(255) not null,
primary key (id)
);
INSERT INTO song (id, album, artist_name, file_path, release_year, song_name, genre) VALUES
(1, 'Rocks', 'aerosmith', './src/test/java/Rift/Radio/songMP3Test/Aerosmith - Back In The Saddle (Audio).mp3', 1976, 'back in the saddle', 'hard Rock'),
( 2, 'Garage Inc.', 'Metallica', './src/test/java/Rift/Radio/songMP3Test/Metallica - Whiskey in the jar.mp3', 1998, 'Whiskey in the jar', 'Heavy Metal'),
( 3, 'Bad to the Bone', 'George Thorogood', './src/test/java/Rift/Radio/songMP3Test/George Thorogood & The Destroyers - Bad To The Bone.mp3', 1982, 'Bad to the Bone', 'Hard Rock/Blues'),
( 4, 'Eliminator', 'ZZ Top', './src/test/java/Rift/Radio/songMP3Test/ZZ Top Sharp Dressed Man.mp3', 1983, 'Sharp Dressed Man', 'Rock/Blues Rock'),
( 5, 'Crash and Burn', 'Pat Travers', './src/test/java/Rift/Radio/songMP3Test/Snortin’ Whiskey.mp3', 1980, 'Snorting Whiskey', 'Rock'),
( 6, 'Power Up', 'AC DC', './src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3', 2020, 'Shot in the dark', 'Power Up');