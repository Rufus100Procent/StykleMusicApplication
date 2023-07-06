package AWS.File.hosting.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Playlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @SequenceGenerator(
            name = "playlist_sequence",
            sequenceName = "playlist_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "playlist_sequence"
    )
    @Column(
            name = "Id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "playlist_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String playlistName;

    //consutructor for the test class
    public Playlist(String s) {

    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", playlistName='" + playlistName + '\'' +
                '}';
    }
}
