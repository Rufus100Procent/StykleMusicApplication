//package AWS.File.hosting;
//
//import Model.Rift.Radio.Artist;
//import Model.Rift.Radio.Song;
//import AWS.File.hosting.Repository.ArtistRepository;
//import Repository.Rift.Radio.SongRepository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseInitializer implements CommandLineRunner {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    private ArtistRepository artistRepository;
//
//    @Autowired
//    private SongRepository songRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        // Create artists
//        Artist artist1 = new Artist("Metallica");
//        Artist artist2 = new Artist("Billy Idol");
//        Artist artist3 = new Artist("Aerosmith");
//        Artist artist4 = new Artist("AC/DC");
//        Artist artist5 = new Artist("Airbourne");
//
//        // Save artists to the database
//        artistRepository.save(artist1);
//        artistRepository.save(artist2);
//        artistRepository.save(artist3);
//        artistRepository.save(artist4);
//        artistRepository.save(artist5);
//
//        Song song1 = new Song("Enter Sandman",  artist1, "1991");
//        Song song2 = new Song("Master of the Puppets",artist1, "1986");
//        Song song3 = new Song("For Whom the Bell Tolls - Remastered",artist1, "1984");
//        Song song4 = new Song("Rebel Yell - Remastered", artist2, "1983" );
//        Song song5 = new Song("Back in the Saddle", artist3, "1976" );
//        Song song6 = new Song("Fire Your Guns",artist4 ,"1990");
//        Song song7 = new Song("Hells Bells", artist4, "1980" );
//        Song song8 = new Song("Breaking Outta Hell",artist5,  "2016");
//
//
//        // Save songs to the database
//        songRepository.save(song1);
//        songRepository.save(song2);
//        songRepository.save(song3);
//        songRepository.save(song4);
//        songRepository.save(song5);
//        songRepository.save(song6);
//        songRepository.save(song7);
//        songRepository.save(song8);
//    }
//}
