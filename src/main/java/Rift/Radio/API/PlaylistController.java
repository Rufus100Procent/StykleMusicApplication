package Rift.Radio.API;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Playlist> createPlaylist(@RequestParam("playlistName") String playlistName,
                                                   @RequestParam("songId") Long songId) {
        Playlist playlist = playlistService.createPlaylist(playlistName, songId);
        return ResponseEntity.ok(playlist);
    }

//    @GetMapping
//    public String getAllPlaylists(Model model) {
//        List<Playlist> playlists = playlistService.getAllPlaylists();
//        model.addAttribute("playlists", playlists);
//        return "playlist";
//    }
//
//    @PostMapping("/{playlistId}/delete")
//    public String deletePlaylist(@PathVariable Long playlistId) {
//        playlistService.deletePlaylist(playlistId);
//        return "redirect:/playlists";
//    }
}
