package Rift.Radio.Repository;

import Rift.Radio.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

//    Optional<Song> findBySongName(String songName);
//
//    @Query("SELECT s.songName, a.artistName, al.albumName, s.releaseYear FROM Song s JOIN s.artist a JOIN s.album al")
//    List<Object[]> findAllWithDetails();
}

