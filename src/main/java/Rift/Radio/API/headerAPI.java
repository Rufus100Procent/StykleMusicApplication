package Rift.Radio.API;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class headerAPI {

    @GetMapping("/")
    public String bar(){
        return "About";
    }
    @GetMapping("/test")
    public String dashboard() {
        // Logic for handling the "Dashboard" page
        return "XP"; // Replace with the actual HTML file for the Dashboard
    }

    @GetMapping("logout")
    public String saaa(){
        return "auth/login";
    }
    @GetMapping("/playlist")
    public String playlist() {
        // Logic for handling the "Playlist" page
        return "playlist"; // Replace with the actual HTML file for the Playlist
    }

    @GetMapping("/feedback")
    public String feedback() {
        // Logic for handling the "Feedback" page
        return "feedback"; // Replace with the actual HTML file for the Feedback
    }


    @GetMapping("/search")
    public String search() {
        // Logic for handling the "Search" page
        return "search"; // Replace with the actual HTML file for the Search
    }

    @GetMapping("/home")
    public String profile() {
        // Logic for handling the "Profile" page
        return "/"; // Replace with the actual HTML file for the Profile
    }
}
