package Rift.Radio.api;

import Rift.Radio.error.SongException;
import Rift.Radio.model.Song;
import Rift.Radio.service.SongService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }



    @GetMapping
    public ResponseEntity<?> getAllSongs(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize) {
        try {
            List<Song> songs = songService.getAllSongs(page, pageSize);
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("songName") @NotBlank String songName,
            @RequestParam("artistName") @NotBlank String artistName,
            @RequestParam("album") String album,
            @RequestParam("releaseYear") @Min(1800) @Max(2023) int releaseYear,
            @RequestParam("genre") String genre) {
        try {
            Song song = songService.uploadSong(file, songName, artistName, album, releaseYear, genre);
            return ResponseEntity.ok(song);
        } catch (SongException e) {
            return handleSongException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @GetMapping("/{id}/path")
    public ResponseEntity<?> getSongPath(@PathVariable Long id) {
        try {
            String path = songService.getSongPath(id);
            return ResponseEntity.ok(path);
        } catch (SongException e) {
            return handleSongException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> getSongFile(@PathVariable Long id) {
        try {
            Resource resource = songService.getSongFile(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (SongException e) {
            return handleSongException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.ok().build();
        } catch (SongException e) {
            return handleSongException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> editSong(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("songName") @NotBlank String songName,
            @RequestParam("artistName") @NotBlank String artistName,
            @RequestParam("album") String album,
            @RequestParam("releaseYear") @Min(1900) @Max(2023) int releaseYear,
            @RequestParam("genre") String genre) {
        try {
            Song updatedSong = songService.editSong(id, file, songName, artistName, album, releaseYear, genre);
            return ResponseEntity.ok(updatedSong);
        } catch (SongException e) {
            return handleSongException(e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    @GetMapping("/{id}/download")
    public void downloadSong(@PathVariable Long id, HttpServletResponse response) {
        try {
            songService.downloadSong(id, response);
        } catch (SongException e) {
            response.setStatus(mapSongExceptionStatus(e));
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private ResponseEntity<?> handleSongException(SongException e) {
        String msg = e.getMessage();
        if ("Song name already exists".equals(msg) || "MP3 file already uploaded".equals(msg)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } else if ("Song not found".equals(msg) || "Song file not found".equals(msg)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        } else if ("Failed to upload the song".equals(msg) || "Failed to update the song".equals(msg)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    private int mapSongExceptionStatus(SongException e) {
        String msg = e.getMessage();
        if ("Song name already exists".equals(msg) || "MP3 file already uploaded".equals(msg)) {
            return HttpStatus.BAD_REQUEST.value();
        } else if ("Song not found".equals(msg) || "Song file not found".equals(msg)) {
            return HttpStatus.NOT_FOUND.value();
        } else if ("Failed to upload the song".equals(msg) || "Failed to update the song".equals(msg)) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
}
