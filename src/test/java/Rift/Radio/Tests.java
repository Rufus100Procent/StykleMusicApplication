package Rift.Radio;

import Rift.Radio.Model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.addAll;



public class Tests {
    protected final ObjectMapper OM = new ObjectMapper();
    protected final static String NEW_ARTIST_NAME = "George Thorogood";
    protected final static String NAME_DUPLICATE = "Duplicate";
    protected final static String NAME_NONEXISTENT = "Nonexistent";
    protected final static String NEW_TRACK_NAME = " Bad to the Bone";

    protected static Song SONG1 = new Song(62L, "back in the saddle", "aerosmith", "Rocks", "hard Rock", 1976, "./LocalStorage/MP3/Aerosmith - Back In The Saddle (Audio).mp3");
    protected static Song SONG2 = new Song(102L, "Whiskey in the jar", "Metalica", " Garage Inc.", "Heavy Metal", 1998, "./LocalStorage/MP3/Metallica - Whiskey in the jar.mp3");
    protected final static Song SONG3 = new Song(73L, "Peer Günt - Backseat", "Peer Günt", "Backseat", "hard Rock", 1986, "./LocalStorage/MP3/Peer Günt  Backseat.mp3");

    protected  static HashMap<Long, Song> SONGS_MAP;
    protected static List<String> ARTIST_NAMES;
    protected static List<Song> SONGS;

    @BeforeAll
    protected static void beforeAll() {
        SONGS_MAP = new HashMap<>();
        SONGS_MAP.put(SONG1.getId(), SONG1);
        SONGS_MAP.put(SONG2.getId(), SONG2);
        SONGS_MAP.put(SONG3.getId(), SONG3);

        System.out.println(SONGS_MAP);

        ARTIST_NAMES = new ArrayList<>();
        ARTIST_NAMES.add(SONG1.getArtistName());
        ARTIST_NAMES.add(SONG2.getArtistName());
        ARTIST_NAMES.add(SONG3.getArtistName());


        System.out.println(ARTIST_NAMES);
        SONGS = new ArrayList<>(SONGS_MAP.values());
    }


}
