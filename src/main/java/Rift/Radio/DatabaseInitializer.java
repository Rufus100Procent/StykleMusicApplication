//package Rift.Radio;

//import Rift.Radio.Model.Song;
//import Rift.Radio.Repository.SongRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class DatabaseInitializer implements CommandLineRunner {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    @Autowired
//    private SongRepository songRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//
//        List<Song> songs = new ArrayList<>();
//
//        songs.add(new Song("songname1", "artist1", "album1", "genre1", 1991, "/path/to/song1.mp3"));
//        songs.add(new Song("songname2", "artist2", "album2", "genre2", 1992, "/path/to/song2.mp3"));
//        songs.add(new Song("songname3", "artist3", "album3", "genre3", 1993, "/path/to/song3.mp3"));
//        // Add more songs as needed
//
//        // Save songs to the database
//        songRepository.saveAll(songs);
//    }

//}
