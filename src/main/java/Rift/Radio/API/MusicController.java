package Rift.Radio.API;

import Rift.Radio.Model.Song;
import Rift.Radio.Service.MusicService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public String uploadForm() {
        return "uploadForm";
    }


    @GetMapping("/songs")
    @ResponseBody
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = musicService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/songs/{id}/file")
    public ResponseEntity<byte[]> downloadSong(@PathVariable("id") Long id) throws IOException {
        Optional<Song> songOptional = musicService.getSongById(id);
        if (songOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Song song = songOptional.get();
        String filePath = song.getFilePath();
        Path file = Path.of(filePath);

        if (!Files.exists(file) || !Files.isReadable(file)) {
            return ResponseEntity.notFound().build();
        }

        byte[] fileBytes = Files.readAllBytes(file);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((MediaType.APPLICATION_OCTET_STREAM));
        headers.setContentDisposition(ContentDisposition.attachment().filename(song.getSongName()).build());

        return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public String uploadSong(@RequestParam("file") MultipartFile file,
                             @RequestParam("songName") String songName,
                             @RequestParam("artist") String artist,
                             @RequestParam("releaseYear") int releaseYear,
                             @RequestParam(value = "album", required = false) String album,
                             Model model) {
        try {
            String filePath = musicService.uploadSong(file, songName, artist, releaseYear, album);
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