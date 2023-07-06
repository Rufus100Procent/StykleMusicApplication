package AWS.File.hosting;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Track;
import AWS.File.hosting.Repository.ArtistRepository;
import AWS.File.hosting.Repository.SongRepository;
import AWS.File.hosting.Repository.TrackRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Main  {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}
