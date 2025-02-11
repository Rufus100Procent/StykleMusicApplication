package Rift.Radio.model;

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


}
