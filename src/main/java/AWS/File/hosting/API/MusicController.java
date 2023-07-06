package AWS.File.hosting.API;

import AWS.File.hosting.Model.Artist;
import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.ArtistRepository;
import AWS.File.hosting.Repository.SongRepository;
import AWS.File.hosting.Service.MusicService;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.List;
@Controller
public class MusicController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private MusicService songService;

    @GetMapping("/")
    public String uploadForm() {
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String uploadSong(@RequestParam("file") MultipartFile file,
                             @RequestParam("songName") String songName,
                             @RequestParam("artist") String artist,
                             @RequestParam("releaseYear") int releaseYear,
                             @RequestParam(value = "album", required = false) String album,
                             Model model) {
        try {
            String filePath = songService.uploadSong(file, songName, artist, releaseYear, album);
            model.addAttribute("message", "Song uploaded successfully!");
            model.addAttribute("songName", songName);
            model.addAttribute("artist", artist);
            model.addAttribute("releaseYear", releaseYear);
            model.addAttribute("album", album);
            model.addAttribute("filePath", filePath);
        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload the song.");
        }

        return "uploadForm";
    }
}