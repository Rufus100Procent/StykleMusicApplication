package Rift.Radio;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ContextTest  extends Tests{

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach()
    {
        String sql = """
            CREATE TABLE songs (
                id INT PRIMARY KEY,
                album TEXT,
                artist_name TEXT,
                file_path TEXT,
                release_year INT,
                song_name TEXT,
                genre TEXT
            );

            INSERT INTO songs (id, album, artist_name, file_path, release_year, song_name, genre) VALUES
                (62, 'Rocks', 'aerosmith', './LocalStorage/MP3/Aerosmith - Back In The Saddle (Audio).mp3', 1976, 'back in the saddle', 'hard Rock'),
                (102, 'Garage Inc.', 'Metallica', './LocalStorage/MP3/Metallica - Whiskey in the jar.mp3', 1998, 'Whiskey in the jar', 'Heavy Metal'),
                (73, 'Backseat', 'Peer Günt', './LocalStorage/MP3/Peer Günt  Backseat.mp3', 1986, 'Peer Günt - Backseat', 'hard Rock'),
                (63, 'Rocks', 'Billy Idol', './LocalStorage/MP3/Billy Idol - Rebel Yell (Official Music Video).mp3', 1983, 'Robel Yell', 'Rock/Pop'),
                (61, 'Back in Black', 'AC DC', './LocalStorage/MP3/AC DC - Shoot To Thrill.mp3', 1980, 'Shoot to Trill', 'hard Rock'),
                (60, 'The Razors Edge', 'AC DC', './LocalStorage/MP3/AC DC - Fire Your Guns (Official Audio).mp3', 1990, 'Fire you guns', 'hard Rock'),
                (64, 'Metallica', 'Metallica', './LocalStorage/MP3/Enter Sandman (Remastered).mp3', 1991, 'Enter the sand', 'Metal'),
                (66, 'Ride the Lightning', 'Metallica', './LocalStorage/MP3/For Whom The Bell Tolls (Remastered).mp3', 1984, 'For Whom the Bells Told', 'Metal'),
                (67, 'Ride the Lightning', 'Metallica/Ray Davies', './LocalStorage/MP3/You Really Got Me.mp3', 1964, 'You Really Got Me', 'Hard Rock'),
                (68, 'Bad to the Bone', 'George Thorogood', './LocalStorage/MP3/George Thorogood & The Destroyers - Bad To The Bone.mp3', 1982, 'Bad to the Bone', 'Hard Rock/Blues'),
                (65, 'Eliminator', 'ZZ Top', './LocalStorage/MP3/ZZ Top Sharp Dressed Man.mp3', 1983, 'Sharp Dressed Man', 'Rock/Blues Rock'),
                (69, 'Morrison Hotel', 'Doors/Crystal Method', './LocalStorage/MP3/Roadhouse Blues.mp3', 1970, 'RoadHouse Blues', 'Rock'),
                (75, 'Drones', 'Muse', './LocalStorage/MP3/Psycho - Muse (Lyrics).mp3', 2015, 'Pyscho', 'hard Rock'),
                (103, 'Crash and Burn', 'Pat Travers', './LocalStorage/MP3/Snortin’ Whiskey.mp3', 1980, 'Snorting Whiskey', 'Rock'),
                (109, 'Power Up', 'AC DC', './LocalStorage/MP3/AC DC - Shot In The Dark (Official Audio).mp3', 2020, 'Shot in the dark', 'Power Up'),
                (110, 'Back in Black', 'AC DC', './LocalStorage/MP3/AC DC - Hells Bells (Official Video).mp3', 1980, 'hells bells', 'hard Rock'),
                (111, 'The Game', 'Queen', './LocalStorage/MP3/Queen - Another One Bites The Dust [Lyrics].mp3', 1980, 'another one bites the dust', 'Funck Rock'),
                (72, 'Cherry Pie', 'Warrant', './LocalStorage/MP3/Cherry Pie.mp3', 1990, 'Cherry Pie', 'Klassisk rock'),
                (70, 'Out For Blood', 'Valley Of Wolves', './LocalStorage/MP3/Chosen One - Valley of Wolves (LYRICS).mp3', 2018, 'Chosen One', 'Blue/Rock'),
                (71, 'Machine Head', 'Deep Purple', './LocalStorage/MP3/Deep Purple - Smoke on the Water (Audio).mp3', 1972, 'Smoke On the Water', 'Klassisk rock');
            """;
        jdbcTemplate.update(sql);
    }
    @AfterEach
    void afterEach() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS songs");

    }
}
