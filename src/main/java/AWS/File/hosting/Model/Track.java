package AWS.File.hosting.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class Track {

    @Id
    @SequenceGenerator(
            name = "artist_sequence",
            sequenceName = "track_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "track_sequence"
    )
    @Column(
            name = "trackId",
            updatable = false
    )
    private Long trackId;

    @Column(
            name = "track_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    public Track() {

    }

    public Track(String trackName) {

    }
}
