package Rift.Radio.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String songName;
    @Column(nullable = false)
    private String artistName;

    private String album;
    private String genre;
    @Column(nullable = false)
    private int releaseYear;
    @Column(nullable = false)
    private String filePath;

    public Song() {
    }

    //Test purpose
    public Song(long l, String sampleSong, String sampleArtist, String sampleAlbum, int i, String pop, String s) {

    }

    public Long getId() {
        return id;
    }

    public Song(String songName, String artistName, String album, String genre, int releaseYear, String filePath) {
        this.songName = songName;
        this.artistName = artistName;
        this.album = album;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.filePath = filePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
