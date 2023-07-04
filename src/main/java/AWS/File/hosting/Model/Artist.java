package AWS.File.hosting.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int artistId;
    private String artistName;

    public Artist(String artistName) {
        this.artistName = artistName;
    }

    public Artist() {

    }
}
