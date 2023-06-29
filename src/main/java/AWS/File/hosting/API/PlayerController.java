package AWS.File.hosting.API;

import AWS.File.hosting.Service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PlayerController {
    private PlaylistService playlistService;

    public PlayerController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

//    @GetMapping("/sfsd")
//    public String showPlaylistPage(Model model) {
//        List<String> playlists = playlistService.getAllPlaylists();
//        model.addAttribute("playlists", playlists);
//        return "navigationbar/navigationbar";
//    }

    @PostMapping("/create")
    public String createPlaylist(@RequestParam("playlistName") String playlistName, Model model) {
        boolean created = playlistService.createPlaylist(playlistName);
        if (!created) {
            model.addAttribute("errorMessage", playlistName + " already exists. Please choose a different name.");
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteSelectedPlaylists(@RequestParam(value = "selectedPlaylists", required = false) List<String> selectedPlaylists) {
        playlistService.deletePlaylists(selectedPlaylists);
        return "redirect:/";
    }

    @GetMapping("/playlist/{name}")
    public String showPlaylistDetails(@PathVariable("name") String playlistName, Model model) {
        // Add code to retrieve and pass the playlist details to the model
        // You can fetch the details from a database or any other data source
        // For now, we'll pass a dummy value to demonstrate the concept
        String playlistDetails = "This is the playlist: " + playlistName;
        model.addAttribute("playlistDetails", playlistDetails);

        return "redirect:/";
    }
}
