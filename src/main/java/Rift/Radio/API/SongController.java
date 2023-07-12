package Rift.Radio.API;

import Rift.Radio.MP3FileExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import Rift.Radio.SongNameExistsException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("/songs")
@Validated
public class SongController {
    private final SongService songService;
    private final SongRepository songRepository;

    @Autowired
    public SongController(SongService songService, SongRepository songRepository) {
        this.songService = songService;
        this.songRepository = songRepository;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Song> getAllSongs(@RequestParam(defaultValue = "0") @Min(0) int page,
                                  @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        return songRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }


    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

//    @GetMapping("/search")
//    @ResponseBody
//    public List<Song> searchSongs(@RequestParam("keyword") String keyword) {
//        return songRepository.searchSongs(keyword);
//    }
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("songName") @NotBlank String songName,
                                        @RequestParam("artistName") @NotBlank String artistName,
                                        @RequestParam("album") String album,
                                        @RequestParam("releaseYear") @Min(1900) @Max(2100) int releaseYear) {
        try {
            Song song = songService.uploadSong(file, songName, artistName, album, releaseYear);
            return ResponseEntity.ok(song);
        } catch (SongNameExistsException e) {
            return ResponseEntity.badRequest().body("Song name already exists");
        } catch (MP3FileExistsException e) {
            return ResponseEntity.badRequest().body("MP3 file already uploaded");
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
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        try {
            songService.deleteSong(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/edit")
    @ResponseBody
    public ResponseEntity<?> editSong(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("songName") @NotBlank String songName,
            @RequestParam("artistName") @NotBlank String artistName,
            @RequestParam("album") String album,
            @RequestParam("releaseYear") @Min(1900) @Max(2100) int releaseYear) {
        try {
            Song updatedSong = songService.editSong(id, file, songName, artistName, album, releaseYear);
            return ResponseEntity.ok(updatedSong);
        } catch (SongNameExistsException e) {
            return ResponseEntity.badRequest().body("Song name already exists");
        } catch (MP3FileExistsException e) {
            return ResponseEntity.badRequest().body("MP3 file already uploaded");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
