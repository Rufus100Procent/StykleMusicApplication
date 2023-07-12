package Rift.Radio.Service;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.PlaylistRepository;
import Rift.Radio.Repository.SongRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    public Playlist createPlaylist(String playlistName) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        return playlistRepository.save(playlist);
    }

    public void addSongToPlaylist(Long playlistId, Long songId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        Optional<Song> songOptional = songRepository.findById(songId);

        if (playlistOptional.isPresent() && songOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            Song song = songOptional.get();

            if (!playlist.getSongs().contains(song)) {
                playlist.addSong(song);
                playlistRepository.save(playlist);
            }
        } else {
            throw new NotFoundException("Playlist or Song not found");
        }
    }

    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        Optional<Song> songOptional = songRepository.findById(songId);

        if (playlistOptional.isPresent() && songOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            Song song = songOptional.get();

            if (playlist.getSongs().contains(song)) {
                playlist.removeSong(song);
                playlistRepository.save(playlist);
            }
        } else {
            throw new NotFoundException("Playlist or Song not found");
        }
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

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Playlist not found"));
    }
}
