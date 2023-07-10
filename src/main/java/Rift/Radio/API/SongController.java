package Rift.Radio.API;

import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/songs")
public class SongController {
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("songName") String songName,
                                        @RequestParam("artistName") String artistName,
                                        @RequestParam("album") String album,
                                        @RequestParam("releaseYear") int releaseYear) {
        try {
            Song song = songService.uploadSong(file, songName, artistName, album, releaseYear);
            return ResponseEntity.ok(song);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the song");
        }
    }

    @GetMapping("/{id}/path")
    @ResponseBody
    public String getSongPath(@PathVariable Long id) {
        return songService.getSongPath(id);
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> getSongFile(@PathVariable Long id) {
        try {
            Resource resource = songService.getSongFile(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
