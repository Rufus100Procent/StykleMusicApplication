package AWS.File.hosting.API;

import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.SongRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SongController {
        private final SongRepository songRepository;

        public SongController(SongRepository songRepository) {
            this.songRepository = songRepository;
        }

        @GetMapping("/songss")
        public String getSongs(Model model) {
            // Specify the path to your songs folder
            String songsFolderPath = "src/main/resources/static/songs/";

            // Get a list of all the MP3 files in the folder
            File[] files = new File(songsFolderPath).listFiles((dir, name) -> name.endsWith(".mp3"));

            // Create a list to hold the song information
            List<Song> songs = new ArrayList<>();

            if (files != null) {
                // Iterate through each file and extract the song information
                for (File file : files) {
                    String fileName = file.getName();
                    String songName = fileName.substring(0, fileName.lastIndexOf('.'));
                    String[] songInfo = songName.split(" - ");

                    if (songInfo.length >= 3) {
                        String track = songInfo[1];
                        String releaseYear = songInfo[2];
                        Song song = new Song(songName, track, releaseYear);
                        songs.add(song);

                        // Save the song to the database
                        songRepository.save(song);
                    }
                }
            }

            // Add the song list to the model
            model.addAttribute("songs", songs);

            // Return the name of the HTML view to render
            return "songss";
        }
    }

