//package AWS.File.hosting.API;
// import AWS.File.hosting.Model.Song;
// import AWS.File.hosting.Service.SongService;
// import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//@Controller
//@RequestMapping("/player")
//public class PlayerController {
//    private final SongService songService;
//    @Autowired
//    public PlayerController(SongService songService) {
//        this.songService = songService;
//    }
//
//        @GetMapping("/songs")
//        public String displaySongs(Model model) {
//            List<Song> songs = songService.getAllSongs();
//            model.addAttribute("songs", songs);
//            return "songs";
//        }
//
//        @GetMapping("/songs/{id}")
//        public String playSong(@PathVariable("id") int id, Model model) {
//            Song song = songService.getSongById(id);
//            model.addAttribute("song", song);
//            return "play";
//        }
//
//        @GetMapping("/songs/next/{id}")
//        public String playNextSong(@PathVariable("id") int currentSongId, Model model) {
//            Song nextSong = songService.getNextSong(currentSongId);
//            model.addAttribute("song", nextSong);
//            return "play";
//        }
//
//        @GetMapping("/songs/previous/{id}")
//        public String playPreviousSong(@PathVariable("id") int currentSongId, Model model) {
//            Song previousSong = songService.getPreviousSong(currentSongId);
//            model.addAttribute("song", previousSong);
//            return "play";
//        }
//
//        // Other methods for handling song upload, deletion, etc.
//    }
