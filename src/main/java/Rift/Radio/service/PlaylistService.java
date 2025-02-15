package Rift.Radio.service;

import Rift.Radio.error.ErrorType;
import Rift.Radio.error.PlaylistException;
import Rift.Radio.model.Playlist;
import Rift.Radio.model.Song;
import Rift.Radio.repository.PlaylistRepository;
import Rift.Radio.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        if (playlistRepository.existsByName(playlist.getName())) {
            throw new PlaylistException(ErrorType.PLAYLIST_ALREADY_EXISTS, "Playlist name already exists");
        }
        return playlistRepository.save(playlist);
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException(ErrorType.PLAYLIST_NOT_FOUND, "Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new PlaylistException(ErrorType.SONG_NOT_FOUND, "Song not found"));
        if (playlist.getSongs().contains(song)) {
            throw new PlaylistException(ErrorType.SONG_ALREADY_IN_PLAYLIST, "Song already in playlist");
        }
        playlist.getSongs().add(song);
        return playlistRepository.save(playlist);
    }

    public Playlist deleteSongFromPlaylist(Long playlistId, Long songId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException(ErrorType.PLAYLIST_NOT_FOUND, "Playlist not found"));
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new PlaylistException(ErrorType.SONG_NOT_FOUND, "Song not found"));
        if (!playlist.getSongs().contains(song)) {
            throw new PlaylistException(ErrorType.SONG_NOT_IN_PLAYLIST, "Song not in playlist");
        }
        playlist.getSongs().remove(song);
        return playlistRepository.save(playlist);
    }

    public List<Song> listSongsInPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException(ErrorType.PLAYLIST_NOT_FOUND, "Playlist not found"));
        return new ArrayList<>(playlist.getSongs());
    }

    public List<Playlist> listAllPlaylists() {
        return playlistRepository.findAll();
    }

}
