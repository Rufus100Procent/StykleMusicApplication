//package AWS.File.hosting.Service;
//
//import AWS.File.hosting.Model.Artist;
//import AWS.File.hosting.Model.Song;
//import AWS.File.hosting.Model.Track;
//import AWS.File.hosting.Repository.SongRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
// public class SongService {
//    private static final String MP3_FOLDER = "src/main/resources/static/songs/";
//
//    private final SongRepository songRepository;
//
//    @Autowired
//    public SongService(SongRepository songRepository) {
//        this.songRepository = songRepository;
//    }
//
//    @Transactional
//    public void saveSongsToDatabase() {
//        File folder = new File(MP3_FOLDER);
//        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));
//        if (files != null) {
//            for (File file : files) {
//                Song song = createSongFromFile(file);
//                songRepository.save(song);
//            }
//        }
//    }
//
//    private Song createSongFromFile(File file) {
//        Song song = new Song();
//        song.setName(file.getName());
//
//        // Populate artist and track information based on your requirements
//        // For example, you can extract artist and track names from the file name or tags
//
//        Artist artist = new Artist();
//        artist.setArtistName("Artist Name");
//        song.setArtist(artist);
//
//        Track track = new Track();
//        track.setName("Track Name");
//        song.setTrack(track);
//
//        return song;
//    }
//}