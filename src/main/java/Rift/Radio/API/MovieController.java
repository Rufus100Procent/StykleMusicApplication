package Rift.Radio.API;

import Rift.Radio.Error.FileExistsException;
import Rift.Radio.Error.FileNameExistsException;
import Rift.Radio.Model.Movie;
import Rift.Radio.Service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Controller
public class MovieController {

    public final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/allMovies")
    @ResponseBody
    public List<Movie> getAllMovies(@RequestParam(defaultValue = "0") @Min(0) int page,
                                   @RequestParam(defaultValue = "50") @Min(1) @Max(100) int pageSize) {
        return movieService.getAllMovies(page, pageSize);
    }

    @GetMapping("/movies")
    public String upload() {
        return "Movies/movies";
    }
    @PostMapping("/movies")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("imageFile") MultipartFile imageFile,
                                        @RequestParam("movieName") @NotBlank String movie_Name,
                                        @RequestParam("producer") @NotBlank String producer,
                                        @RequestParam("releaseYear") @NotBlank String releaseYear) {
        try {
            Movie movie = movieService.upload(imageFile, movie_Name,  producer, Integer.parseInt(releaseYear));
            return ResponseEntity.ok(movie);
        } catch (FileNameExistsException e) {
            return ResponseEntity.badRequest().body("Song name already exists");
        } catch (FileExistsException e) {
            return ResponseEntity.badRequest().body("MP3 file already uploaded");
        }
        }
    }





