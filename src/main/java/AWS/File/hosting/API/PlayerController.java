package AWS.File.hosting.API;

import AWS.File.hosting.Service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PlayerController {
        private final PlaylistService playlistService;

        @Autowired
        public PlayerController(PlaylistService playlistService) {
            this.playlistService = playlistService;
        }


    @GetMapping("/playlists")
    public String getAllPlaylists(Model model) {
        model.addAttribute("playlists", playlistService.getAllPlaylists());
        return "playlist-list";
    }

    @PostMapping("/playlists")
    public String createPlaylist(@RequestParam("playlistName") String playlistName) {
        playlistService.createPlaylist(playlistName);
        return "redirect:/playlists";
    }

    @PostMapping("/playlists/delete")
    public String deletePlaylistByName(@RequestParam("playlistName") String playlistName) {
        playlistService.deletePlaylistByName(playlistName);
        return "redirect:/playlists";
    }

    @PostMapping("/playlists/delete/{id}")
    public String deletePlaylistById(@PathVariable("id") Long id) {
        playlistService.deletePlaylistById(id);
        return "redirect:/playlists";
    }

}
