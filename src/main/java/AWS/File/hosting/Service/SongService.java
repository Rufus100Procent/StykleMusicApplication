package AWS.File.hosting.Service;

import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }

    public List<Song> searchSongs(String query) {
        return songRepository.findBySongNameContainingIgnoreCase(query);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }
}
