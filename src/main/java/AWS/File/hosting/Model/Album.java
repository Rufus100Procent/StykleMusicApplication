package AWS.File.hosting.Model;

import jakarta.persistence.*;

@Entity
@Table(name="ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int albumId;
    @Column(name="ALMUB_NAME", length=50, nullable=false, unique=false)
    private String albumName;
    @Transient
    private int releaseYear;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }




    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
