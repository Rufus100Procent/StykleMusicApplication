package Rift.Radio.API;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Model.Song;
import Rift.Radio.Service.PlaylistService;
import Rift.Radio.Service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final SongService songService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, SongService songService) {
        this.playlistService = playlistService;
        this.songService = songService;
    }

    @GetMapping("/create")
    public String showCreatePlaylistForm() {
        return "create_playlist";
    }

    @PostMapping("/create")
    public String createPlaylist(@RequestParam String playlistName) {
        playlistService.createPlaylist(playlistName);
        return "redirect:/playlists";
    }

    @GetMapping
    public String getAllPlaylists(Model model) {
        List<Playlist> playlists = playlistService.getAllPlaylists();
        model.addAttribute("playlists", playlists);
        return "playlist";
    }

    @PostMapping("/{playlistId}/addSong")
    public String addSongToPlaylist(@PathVariable Long playlistId, @RequestParam(name = "songId", required = false) Long songId) {
        if (songId != null) {
            playlistService.addSongToPlaylist(playlistId, songId);
        }
        return "redirect:/playlists";
    }

    @GetMapping("/{playlistId}")
    public String getPlaylist(@PathVariable Long playlistId, Model model) {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        List<Song> songs = songService.getAllSongs();
        model.addAttribute("playlist", playlist);
        model.addAttribute("songs", songs);
        return "playlist";
    }

    @PostMapping("/{playlistId}/removeSong")
    public String removeSongFromPlaylist(@PathVariable Long playlistId, @RequestParam(name = "songId", required = false) Long songId) {
        if (songId != null) {
            playlistService.removeSongFromPlaylist(playlistId, songId);
        }
        return "redirect:/playlists/{playlistId}";
    }

    @DeleteMapping("/{id}")
    public String deletePlaylistById(@PathVariable Long id) {
        playlistService.deletePlaylistById(id);
        return "redirect:/playlists";
    }

    @DeleteMapping
    public String deletePlaylistByName(@RequestParam String playlistName) {
        playlistService.deletePlaylistByName(playlistName);
        return "redirect:/playlists";
    }
}
