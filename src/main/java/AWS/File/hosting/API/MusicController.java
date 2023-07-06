package AWS.File.hosting.API;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.ArtistRepository;
import AWS.File.hosting.Repository.SongRepository;
import AWS.File.hosting.Service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
public class MusicController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MusicService musicService;

    @GetMapping("/")
    public String listMusic(Model model) {
        List<Song> songs = musicService.getAllSongs();
        List<Artist> artists = musicService.getAllArtists();
        model.addAttribute("songs", songs);
        model.addAttribute("artists", artists);
        return "music";
    }
}