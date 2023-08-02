package Rift.Radio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContextTestTest extends ContextTest {


    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        super.beforeEach(); // Call the beforeEach method of the parent class (ContextTest) to insert data into the database.
    }
    @AfterEach
    void afterEach() {
        jdbcTemplate.update("DELETE FROM song"); // Clear the data from the database after each test.
    }

    @Test
    void testSongDataInTestsClass() {
        // Verify that the songs from the Tests class are not null
        assertNotNull(SONG_SHOT_IN_THE_DARK);
        assertNotNull(SONG_BACK_IN_THE_SADDLE);
        assertNotNull(SONG_SNORTING_WHISKEY);
        assertNotNull(SONG_WHISKEY_IN_THE_JAR);
        assertNotNull(SONG_BAD_IN_THE_BONE);
        assertNotNull(SONG_SHARP_DRESSED_MAN);


        assertEquals("AC DC", SONG_SHOT_IN_THE_DARK.getArtistName());
        assertEquals(2020, SONG_SHOT_IN_THE_DARK.getReleaseYear());
    }

    @Test
    void testSongDataInContextTestClass() {
        // Verify that the songs from the ContextTest class are inserted into the database
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM song", Integer.class);
        assertEquals(6, count); // Assuming 6 songs are inserted in the setup.


    }

    @Test
    void testSongProperties() {
        // Test the properties of the Song class
        assertEquals("Shot in the dark", SONG_SHOT_IN_THE_DARK.getSongName());
        assertEquals("hard Rock", SONG_BACK_IN_THE_SADDLE.getGenre());
        assertEquals("Metalica", SONG_WHISKEY_IN_THE_JAR.getArtistName());
    }

}