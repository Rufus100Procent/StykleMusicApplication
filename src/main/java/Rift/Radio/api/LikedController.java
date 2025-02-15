package Rift.Radio.api;

import Rift.Radio.model.LikedSong;
import Rift.Radio.model.Song;
import Rift.Radio.service.LikedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/liked")
@CrossOrigin("*")
public class LikedController {

    private final LikedService likedService;

    @Autowired
    public LikedController(LikedService likedService) {
        this.likedService = likedService;
    }

    @PostMapping("/{songId}")
    public ResponseEntity<LikedSong> likeSong(@PathVariable Long songId) {
        LikedSong likedSong = likedService.likeSong(songId);
        return ResponseEntity.status(HttpStatus.CREATED).body(likedSong);
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllLikedSongs() {
        List<Song> likedSongs = likedService.getAllLikedSongs();
        return ResponseEntity.ok(likedSongs);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> removeLikedSong(@PathVariable Long songId) {
        likedService.removeLikedSong(songId);
        return ResponseEntity.ok().build();
    }

}
