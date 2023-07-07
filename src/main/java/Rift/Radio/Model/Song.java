package Rift.Radio.Model;


import jakarta.persistence.*;

@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String songName;
    private String artist;
    private String album; // Optional field for album name
    private int releaseYear;
    private String filePath; // Added field to store the file path

    public Song() {
    }

    public Song(String songName, String artist, String album, int releaseYear, String filePath) {
        this.songName = songName;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
        this.filePath = filePath;
    }

    // ...


    public void setAlbum(String album) {
        this.album = album;
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

    public String getSongName() {
        return songName;
    }

    public void setSongName(String title) {
        this.songName = title;
    }



    public void setArtist(String artist) {
        this.artist = artist;
    }
}