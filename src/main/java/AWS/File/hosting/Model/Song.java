package AWS.File.hosting.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "song")

 public class Song {
    @Id
    @SequenceGenerator(
            name = "songs_sequence",
            sequenceName = "songs_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "songs_sequence"
    )
    private Long songId;
    private String name;


    private String releaseYear;

    @ManyToOne
    private Artist artist;



    public Song() {
    }

    public Song(String name, String releaseYear, Artist artist) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }
    public Song(String songName, String track, String releaseYear) {

    }

    public Song(String enterSandman, int i, String hardRock, Artist artist1) {

    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

}