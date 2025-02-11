package Rift.Radio.api;

import Rift.Radio.error.MP3FileExistsException;
import Rift.Radio.model.Song;
import Rift.Radio.service.SongService;
import Rift.Radio.error.SongNameExistsException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * Endpoint to display information about the MusicApp, Welcome Page.x
     *
     * @return The name of the view page "About".
     */

    @GetMapping("/")
    public String home() {
        return "About";
    }

    /**
     * Endpoint to get a paginated list of all songs.
     *
     * @param page     The page number (default is 0).
     * @param pageSize The number of songs per page (default is 50, maximum is 100).
     * @return A list of songs on the requested page.
     */
    @GetMapping("/all")
    @ResponseBody
    public List<Song> getAllSongs(@RequestParam(defaultValue = "0") @Min(0) int page,
                                  @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize) {
        return songService.getAllSongs(page, pageSize);
    }


    @GetMapping("/songs")
    public String upload() {
        return "musicApp";
    }

    /**
     * Endpoint to upload a new song with metadata and file.
     *
     * @param file        The multipart file representing the song.
     * @param songName    The name of the song.
     * @param artistName  The name of the artist.
     * @param album       The album of the song (optional).
     * @param releaseYear The release year of the song.
     * @param genre       The genre of the song.
     * @return The response entity with the uploaded song entity if successful.
     * @throws SongNameExistsException If the song name already exists in the repository.
     * @throws MP3FileExistsException  If the MP3 file with the same path already exists in the repository.
     */
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

    /**
     * Endpoint to get the file path of a song by its ID.
     *
     * @param id The ID of the song.
     * @return The file path of the song.
     */
    @GetMapping("/{id}/path")
    @ResponseBody
    public String getSongPath(@PathVariable Long id) {
        return songService.getSongPath(id);
    }

    /**
     * Endpoint to download the song file by its ID.
     *
     * @param id       The ID of the song.
     */
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

    /**
     * Endpoint to delete a song by its ID.
     *
     * @param id The ID of the song to be deleted.
     * @return The response entity indicating the success of the operation.
     */
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

    /**
     * Endpoint to edit a song's metadata and file by its ID.
     *
     * @param id          The ID of the song to be edited.
     * @param file        The new multipart file representing the song (optional).
     * @param songName    The new name of the song.
     * @param artistName  The new name of the artist.
     * @param album       The new album of the song (optional).
     * @param releaseYear The new release year of the song.
     * @param genre       The new genre of the song.
     * @return The response entity with the updated song entity if successful.
     * @throws SongNameExistsException If the new song name already exists in the repository.
     * @throws MP3FileExistsException  If the new MP3 file with the same path already exists in the repository.
     * @throws NotFoundException      If the song is not found.
     */
    @PutMapping("/{id}/edit")
    @ResponseBody
    public ResponseEntity<?> editSong(@PathVariable Long id,
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

    /**
     * Endpoint to download the song file by its ID.
     *
     * @param id       The ID of the song.
     * @param response The HTTP response object.
     */
    @GetMapping("/{id}/download")
    public void downloadSong(@PathVariable Long id, HttpServletResponse response) {
        songService.downloadSong(id, response);
    }
}
