package AWS.File.hosting.Repository;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByTitle(String fileName);

}