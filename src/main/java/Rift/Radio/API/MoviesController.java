package Rift.Radio.API;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Movies")
public class MoviesController {

    @GetMapping
    public String moviePage(){
        return "Movies/movies";
    }
}
