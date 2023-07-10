package Rift.Radio.Model;

import jakarta.persistence.*;

@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    public Album(String albumName) {

    }

    public Album() {

    }

    public void setAlbumName(String albumName) {

    }

    // Getters and setters

}
