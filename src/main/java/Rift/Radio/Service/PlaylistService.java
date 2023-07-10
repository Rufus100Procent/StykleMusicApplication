package Rift.Radio.Service;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(String playlistName) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        return playlistRepository.save(playlist);
    }
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public void deletePlaylistByName(String playlistName) {
        Optional<Playlist> playlistOptional = playlistRepository.findByPlaylistName(playlistName);
        playlistOptional.ifPresent(playlistRepository::delete);
    }

    public void deletePlaylistById(Long id) {
        playlistRepository.deleteById(id);
    }
}