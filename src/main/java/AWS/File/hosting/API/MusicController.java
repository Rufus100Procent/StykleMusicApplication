package AWS.File.hosting.API;

import AWS.File.hosting.Service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/music")
public class MusicController {
    @Autowired
    private MusicPlayerService musicPlayerService;

    @GetMapping("/")
    public String soundcontroller(){
        return "soundcontrol";
    }

    @GetMapping("/play")
    public String play(Model model) {
        musicPlayerService.play();
        model.addAttribute("isPlaying", musicPlayerService.isPlaying());
        return "music";
    }

    @GetMapping("/pause")
    public String pause(Model model) {
        musicPlayerService.pause();
        model.addAttribute("isPlaying", musicPlayerService.isPlaying());
        return "music";
    }

    @GetMapping("/next")
    public String next(Model model) {
        musicPlayerService.next();
        model.addAttribute("isPlaying", musicPlayerService.isPlaying());
        return "music";
    }

    @GetMapping("/previous")
    public String previous(Model model) {
        musicPlayerService.previous();
        model.addAttribute("isPlaying", musicPlayerService.isPlaying());
        return "music";
    }
}
