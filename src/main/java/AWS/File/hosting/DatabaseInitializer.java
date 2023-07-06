package AWS.File.hosting;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.ArtistRepository;
import AWS.File.hosting.Repository.SongRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private SongRepository songRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Create artists
        Artist artist1 = new Artist("Metallica");
        Artist artist2 = new Artist("Billy Idol");
        Artist artist3 = new Artist("Aerosmith");
        Artist artist4 = new Artist("AC/DC");
        Artist artist5 = new Artist("Airbourne");

        // Save artists to the database
        artistRepository.save(artist1);
        artistRepository.save(artist2);
        artistRepository.save(artist3);
        artistRepository.save(artist4);
        artistRepository.save(artist5);

        Song song1 = new Song("Enter Sandman", "1991", artist1);
        Song song2 = new Song("Master of the Puppets", "1986", artist1);
        Song song3 = new Song("For Whom the Bell Tolls - Remastered", "1984", artist1);
        Song song4 = new Song("Rebel Yell - Remastered", "1983", artist2);
        Song song5 = new Song("Back in the Saddle", "1976", artist3);
        Song song6 = new Song("Fire Your Guns", "1990", artist4);
        Song song7 = new Song("Hells Bells", "1980", artist4);
        Song song8 = new Song("Breaking Outta Hell", "2016", artist5);


        // Save songs to the database
        songRepository.save(song1);
        songRepository.save(song2);
        songRepository.save(song3);
        songRepository.save(song4);
        songRepository.save(song5);
        songRepository.save(song6);
        songRepository.save(song7);
        songRepository.save(song8);
    }
}
