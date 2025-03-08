package Rift.Radio.modal;

import jakarta.persistence.*;


@Entity
@SuppressWarnings("unused")
public class LikedSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "song_id", unique = true, nullable = false)
    private Song song;

    public LikedSong() {}

    public LikedSong(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
