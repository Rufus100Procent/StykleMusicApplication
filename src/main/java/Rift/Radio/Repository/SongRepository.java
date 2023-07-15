package Rift.Radio.Repository;

import Rift.Radio.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsBySongName(String songName);
    boolean existsByFilePath(String filePath);

    boolean existsBySongNameAndIdNot(String songName, Long id);


//    @Query("SELECT s FROM Song s WHERE lower(s.songName) LIKE lower(concat('%', :query, '%')) OR lower(s.artistName) LIKE lower(concat('%', :query, '%')) OR lower(s.album) LIKE lower(concat('%', :query, '%')) OR s.releaseYear LIKE concat('%', :query, '%')")
//    List<Song> searchSongs(@Param("query") String query);
}

