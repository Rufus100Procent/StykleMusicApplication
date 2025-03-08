package Rift.Radio.repository;

import Rift.Radio.modal.LikedSong;
import Rift.Radio.modal.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    Optional<LikedSong> findBySong(Song song);
}
