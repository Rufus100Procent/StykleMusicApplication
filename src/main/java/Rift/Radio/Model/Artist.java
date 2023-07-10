package Rift.Radio.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_id")
    private Long id;

    private String artistName;

    public Artist(String artistName) {

    }

    public Artist() {

    }

    public void setArtistName(String artistName) {

    }

    // Getters and setters
}
