package Rift.Radio.api;

import Rift.Radio.error.PlaylistException;
import Rift.Radio.modal.Playlist;
import Rift.Radio.modal.Song;
import Rift.Radio.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v0/playlists")
@CrossOrigin(origins = "http://localhost:5173")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<List<Playlist>> listAllPlaylists() {
        List<Playlist> playlists = playlistService.listAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist) {
        try {
            Playlist created = playlistService.createPlaylist(playlist);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (PlaylistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist updated = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Playlist> deleteSongFromPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        Playlist updated = playlistService.deleteSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<List<Song>> listSongsInPlaylist(@PathVariable Long playlistId) {
        List<Song> songs = playlistService.listSongsInPlaylist(playlistId);
        return ResponseEntity.ok(songs);
    }

}
