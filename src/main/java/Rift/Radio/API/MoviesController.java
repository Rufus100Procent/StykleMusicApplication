package Rift.Radio.API;

import Rift.Radio.Service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class MoviesController {

    private final MoviesService moviesService;

    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "test";
    }

    @GetMapping("/getImages")
    @ResponseBody
    public List<String> getImages() {
        // Retrieve the three different image URLs from the service
        return moviesService.getImageUrls();
    }
}
