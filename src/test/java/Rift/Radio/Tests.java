package Rift.Radio;

import Rift.Radio.Model.Song;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    private final static String TEST_FILE_DIRECTORY = "src/test/java/Rift/Radio/songMP3Test/";

    protected final static String TEST_MP31 = "AC DC - Shot In The Dark (Official Audio).mp3";
    protected final static String TEST_MP32 = "Snortin’ Whiskey.mp3";
    protected final static String TEST_MP33 = "Aerosmith - Back In The Saddle (Audio).mp3";
    protected final static String TEST_MP34 = "Metallica - Whiskey in the jar.mp3";

    protected final static String DUPLICATE_SONG = "back in the saddle";
    protected final static String NAME_NONEXISTENT = "Nonexistent";

    protected static Song SONG1 = new Song("back in the saddle", "aerosmith", "Rocks", "hard Rock", 1976, TEST_FILE_DIRECTORY + TEST_MP33);
    protected static Song SONG2 = new Song( "Whiskey in the jar", "Metalica", " Garage Inc.", "Heavy Metal", 1998, TEST_FILE_DIRECTORY + TEST_MP34);
    protected static Song SONG3 = new Song("Snorting Whiskey", "Pat Travers", "Crash and Burn", "Rock", 1980, TEST_FILE_DIRECTORY + TEST_MP32);
    protected static Song SONG4 = new Song( "Shot in the dark", "AC DC", "Power Up", "Klassisk rock", 2020, TEST_FILE_DIRECTORY + TEST_MP31);

    protected static List<String> SONG_NAME;
    protected static List<String> ARTIST_NAMES;
    protected static List<String> ALBUM_NAME;
    protected static List<String> GENRE;
    protected static List<Integer> RELEASE_YEAR;
    @BeforeAll
    protected static void beforeAll() {


        SONG_NAME = new ArrayList<>();
        SONG_NAME.add(SONG1.getSongName());
        SONG_NAME.add(SONG2.getSongName());
        SONG_NAME.add(SONG3.getSongName());
        SONG_NAME.add(SONG4.getSongName());


        //Verify
        System.out.println(SONG_NAME);


        ARTIST_NAMES = new ArrayList<>();
        ARTIST_NAMES.add(SONG1.getArtistName());
        ARTIST_NAMES.add(SONG2.getArtistName());
        ARTIST_NAMES.add(SONG3.getArtistName());
        ARTIST_NAMES.add(SONG4.getArtistName());

        //Verify
        System.out.println(ARTIST_NAMES);


        ALBUM_NAME = new ArrayList<>();
        ALBUM_NAME.add(SONG1.getAlbum());
        ALBUM_NAME.add(SONG2.getAlbum());
        ALBUM_NAME.add(SONG3.getAlbum());
        ALBUM_NAME.add(SONG4.getAlbum());

        //Verify
        System.out.println(ALBUM_NAME);



        GENRE = new ArrayList<>();
        GENRE.add(SONG1.getGenre());
        GENRE.add(SONG2.getGenre());
        GENRE.add(SONG3.getGenre());
        GENRE.add(SONG4.getGenre());

        //Verify
        System.out.println(GENRE);


        RELEASE_YEAR = new ArrayList<>();
        RELEASE_YEAR.add(SONG1.getReleaseYear());
        RELEASE_YEAR.add(SONG2.getReleaseYear());
        RELEASE_YEAR.add(SONG3.getReleaseYear());
        RELEASE_YEAR.add(SONG4.getReleaseYear());

        //Verify
        System.out.println(RELEASE_YEAR);

    }

}
