package Rift.Radio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

/*
Verifies both Test, and ContextTest are working correctly
* */
public class ContextTestTest extends ContextTest {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseSetup() {
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM songs", Integer.class);
        assertEquals(6, rowCount, "Number of rows in the 'songs' table should be 6");
    }

    @Test
    void testSongData() {
        // Verify the existence of specific songs in the database
        assertTrue(songExists("aerosmith", "back in the saddle"));
        assertTrue(songExists("Metallica", "Whiskey in the jar"));
        assertTrue(songExists("Pat Travers", "Snorting Whiskey"));
        assertTrue(songExists("AC DC", "Shot in the dark"));
    }

    private boolean songExists(String artistName, String songName) {
        String sql = "SELECT COUNT(*) FROM songs WHERE artist_name = ? AND song_name = ?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, artistName, songName);
        return rowCount > 0;
    }


    @Test
    void testSongGenresAndReleaseYears() {
        assertEquals(6, GENRE.size(), "There should be 4 unique genres");
        assertEquals(6, RELEASE_YEAR.size(), "There should be 4 unique release years");

        // Verify the existence of specific genres and release years
        assertTrue(GENRE.contains("hard Rock"));
        assertTrue(GENRE.contains("Heavy Metal"));
        assertTrue(GENRE.contains("Rock"));
        assertTrue(GENRE.contains("Klassisk rock"));

        assertTrue(RELEASE_YEAR.contains(1976));
        assertTrue(RELEASE_YEAR.contains(1998));
        assertTrue(RELEASE_YEAR.contains(1980));
        assertTrue(RELEASE_YEAR.contains(2020));
    }
    @Test
    void testArtsitNameExistence() {
        //Correct
        assertTrue(ARTIST_NAMES.contains("aerosmith"));
        assertTrue(ARTIST_NAMES.contains("Metalica"));
        assertTrue(ARTIST_NAMES.contains("Pat Travers"));
        assertTrue(ARTIST_NAMES.contains("AC DC"));

    }
    @Test
    void testSongNameExistence() {
        //Correct
        assertTrue(SONG_NAME.contains("back in the saddle"));
        assertTrue(SONG_NAME.contains("Whiskey in the jar"));
        assertTrue(SONG_NAME.contains("Snorting Whiskey"));
        assertTrue(SONG_NAME.contains("Shot in the dark"));

    }
    @Test
    void testSongFilePath() {
        //CORRECT
        assertTrue(SONG1.getFilePath().endsWith(TEST_MP31));
        assertTrue(SONG2.getFilePath().endsWith(TEST_MP32));
        assertTrue(SONG3.getFilePath().endsWith(TEST_MP33));
        assertTrue(SONG4.getFilePath().endsWith(TEST_MP34));
    }

    @Test
    void testSongGenresContainsRightInfo() {
        assertTrue(GENRE.contains("hard Rock"));
        assertTrue(GENRE.contains("Heavy Metal"));
        assertTrue(GENRE.contains("Rock"));
        assertTrue(GENRE.contains("Klassisk rock"));

        assertFalse(GENRE.contains("Heavy "));
        assertFalse(GENRE.contains("rick Metal"));
        assertFalse(GENRE.contains("h Metal"));
        assertFalse(GENRE.contains("Hey Metal"));
    }

    @Test
    void testSongReleaseYearContainsRightInfo() {
        //correct
        assertTrue(RELEASE_YEAR.contains(1976));
        assertTrue(RELEASE_YEAR.contains(1998));
        assertTrue(RELEASE_YEAR.contains(1980));
        assertTrue(RELEASE_YEAR.contains(2020));

        //false
        assertFalse(RELEASE_YEAR.contains(1977));
        assertFalse(RELEASE_YEAR.contains(555));
        assertFalse(RELEASE_YEAR.contains(1970));
        assertFalse(RELEASE_YEAR.contains(2021));
    }

}
