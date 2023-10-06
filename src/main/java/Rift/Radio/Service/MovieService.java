package Rift.Radio.Service;

import Rift.Radio.Error.FileNameExistsException;
import Rift.Radio.Model.Movie;
import Rift.Radio.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MovieService {
    public final MovieRepository movieRepository;
    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public List<Movie> getAllMovies(int page, int pageSize) {
        return movieRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }
    public Movie upload(MultipartFile imageFile, String movie_Name, String producer, int releaseYear){

        // Check if song name already exists
        if (movieRepository.existsByMovieName(movie_Name)) {
            throw new FileNameExistsException("Song name already exists");
        }

        try {
            validateFile(imageFile);

            String fileName = StringUtils.cleanPath(StringUtils.hasText(imageFile.getOriginalFilename()) ? imageFile.getOriginalFilename() : "movie.png");
            String storagePath = "src/main/resources/LocalStorage/MovieImage/";
            String filePath = storagePath + fileName;



            imageFile.transferTo(new File(filePath));

            Movie movie = new Movie();
            movie.setMovieName(movie_Name);
            movie.setMovieName(producer);
            movie.setRelease_year(releaseYear);
            movie.setImagePath(filePath);
            return movieRepository.save(movie);
        } catch (IOException e) {
            // Handle IOException separately
            e.printStackTrace();
            throw new RuntimeException("Failed to upload the movie.png");
        }


    }
    private void validateFile(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new RuntimeException("File is empty");
        }
        long MAX_FILE_SIZE = 104857600;
        if (imageFile.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds the limit");
        }
        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Invalid file format. Only image files are allowed.");
        }
    }


}
