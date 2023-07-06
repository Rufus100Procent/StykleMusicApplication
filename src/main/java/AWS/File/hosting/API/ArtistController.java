//package AWS.File.hosting.API;
//
//import AWS.File.hosting.Model.Artist;
//import AWS.File.hosting.Service.ArtistService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//public class ArtistController {
//
//    private final ArtistService artistService;
//
//    @Autowired
//    public ArtistController(ArtistService artistService) {
//        this.artistService = artistService;
//    }
//
//    @GetMapping("/")
//    public String getAllArtists(Model model) {
//        List<Artist> artists = artistService.getAllArtists();
//        model.addAttribute("artists", artists);
//        return "artists";
//    }
//}