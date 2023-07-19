    package Rift.Radio.API;

    import Rift.Radio.Error.MP3FileExistsException;
    import Rift.Radio.Model.Song;
    import Rift.Radio.Service.SongService;
    import Rift.Radio.Error.SongNameExistsException;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.ws.rs.NotFoundException;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.core.io.Resource;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import javax.validation.constraints.Max;
    import javax.validation.constraints.Min;
    import javax.validation.constraints.NotBlank;
    import java.util.List;

    @Controller
    @Validated
    public class SongController {
        private final SongService songService;

        @Autowired
        public SongController(SongService songService) {
            this.songService = songService;
        }

        @GetMapping("/")
        public String about(){
            return "About";
        }

        @GetMapping("/all")
        @ResponseBody
        public List<Song> getAllSongs(@RequestParam(defaultValue = "0") @Min(0) int page,
                                      @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize) {
            return songService.getAllSongs(page, pageSize);
        }

        @GetMapping("/upload")
        public String upload() {
            return "musicApp";
        }

        @PostMapping("/upload")
        @ResponseBody
        public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                            @RequestParam("songName") @NotBlank String songName,
                                            @RequestParam("artistName") @NotBlank String artistName,
                                            @RequestParam("album") String album,
                                            @RequestParam("releaseYear") @Min(1800) @Max(2023) int releaseYear,
                                            @RequestParam("genre") String genre) {
            try {
                Song song = songService.uploadSong(file, songName, artistName, album, releaseYear, genre);
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
                @RequestParam("releaseYear") @Min(1900) @Max(2023) int releaseYear,
                @RequestParam("genre") String genre) {
            try {
                Song updatedSong = songService.editSong(id, file, songName, artistName, album, releaseYear, genre);
                return ResponseEntity.ok(updatedSong);
            } catch (SongNameExistsException e) {
                return ResponseEntity.badRequest().body("Song name already exists");
            } catch (MP3FileExistsException e) {
                return ResponseEntity.badRequest().body("MP3 file already uploaded");
            } catch (NotFoundException e) {
                return ResponseEntity.notFound().build();
            }
        }
        @GetMapping("/{id}/download")
        public void downloadSong(@PathVariable Long id, HttpServletResponse response) {
            songService.downloadSong(id, response);
        }

    }
