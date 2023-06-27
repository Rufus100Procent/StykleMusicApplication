package AWS.File.hosting.API;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PlayerController {
    private List<String> playlists = new ArrayList<>();

    @GetMapping("/")
    public String showPlaylistPage(Model model) {
        model.addAttribute("playlists", playlists);
        return "playlist";
    }

    @PostMapping("/create")
    public String createPlaylist(@RequestParam("playlistName") String playlistName, Model model) {
        if (playlists.stream().anyMatch(p -> p.equalsIgnoreCase(playlistName.trim()))) {
            model.addAttribute("errorMessage", playlistName + " already exists. Please choose a different name.");
        } else {
            playlists.add(playlistName.trim());
        }
        model.addAttribute("playlists", playlists);
        return "playlist";
    }

    @PostMapping("/delete")
    public String deleteSelectedPlaylists(@RequestParam(value = "selectedPlaylists", required = false) List<String> selectedPlaylists, Model model) {
        if (selectedPlaylists != null) {
            playlists.removeAll(selectedPlaylists);
        }
        model.addAttribute("playlists", playlists);
        return "playlist";
    }
}
