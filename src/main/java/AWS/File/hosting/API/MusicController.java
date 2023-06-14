package AWS.File.hosting.API;

import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {

        private SongService songService;

        public void SongController(SongService songService) {
            this.songService = songService;
        }

    public MusicController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
        public ResponseEntity<List<Song>> getSongs() {
            List<Song> songs = songService.getSongs();
            return ResponseEntity.ok(songs);
        }

        @PostMapping
        public ResponseEntity<Void> uploadSong(@RequestParam String name, @RequestParam String fileName) {
            songService.uploadSong(name, fileName);
            return ResponseEntity.ok().build();
        }

        @GetMapping("/play/{index}")
        public ResponseEntity<Void> playSong(@PathVariable int index) {
            songService.playSong(index);
            return ResponseEntity.ok().build();
        }

        @GetMapping("/next")
        public ResponseEntity<Void> playNextSong() {
            songService.playNextSong();
            return ResponseEntity.ok().build();
        }

        @GetMapping("/previous")
        public ResponseEntity<Void> playPreviousSong() {
            songService.playPreviousSong();
            return ResponseEntity.ok().build();
        }
    }