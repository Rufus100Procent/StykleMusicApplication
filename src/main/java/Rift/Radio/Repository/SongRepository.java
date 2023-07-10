package Rift.Radio.Repository;

import Rift.Radio.Model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsBySongName(String songName);
    boolean existsByFilePath(String filePath);
}

