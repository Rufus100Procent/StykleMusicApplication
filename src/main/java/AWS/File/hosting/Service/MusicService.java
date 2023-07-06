package AWS.File.hosting.Service;

import org.springframework.stereotype.Service;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.ArtistRepository;
import AWS.File.hosting.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }
}