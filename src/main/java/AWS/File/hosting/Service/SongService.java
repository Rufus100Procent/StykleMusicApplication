package AWS.File.hosting.Service;

import AWS.File.hosting.Model.Song;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
 public class SongService {
    private List<Song> songs;
    private int currentSongIndex;
    private String currentSongName;

    public SongService() {
        songs = new ArrayList<>();
        currentSongIndex = 0;
        currentSongName = "";
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void displaySongs() {
        if (songs.isEmpty()) {
            System.out.println("No songs uploaded yet.");
            return;
        }

        for (int i = 0; i < songs.size(); i++) {
            Song song = songs.get(i);
            System.out.println((i + 1) + ". " + song.getName());
        }
    }

    public List<Song> filterSongs(String query) {
        List<Song> filteredSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredSongs.add(song);
            }
        }
        return filteredSongs;
    }

    public void uploadSong(String name, String fileName) {
        String filePath = getFileStoragePath() + File.separator + fileName;
        Song song = new Song(name, filePath);
        songs.add(song);
    }

    public void playSong(int index) {
        if (index == currentSongIndex) {
            // Toggle play/pause
            if (isPaused()) {
                play(songs.get(currentSongIndex).getFilePath());
            } else {
                pause();
            }
            return;
        }

        currentSongIndex = index;
        Song song = songs.get(index);
        String filePath = song.getFilePath();
        play(filePath);
        updateCurrentSong(song.getName());
    }

    public void playNextSong() {
        if (currentSongIndex == songs.size() - 1) {
            currentSongIndex = 0;
        } else {
            currentSongIndex++;
        }
        Song song = songs.get(currentSongIndex);
        String filePath = song.getFilePath();
        play(filePath);
        updateCurrentSong(song.getName());
    }

    public void playPreviousSong() {
        if (currentSongIndex == 0) {
            currentSongIndex = songs.size() - 1;
        } else {
            currentSongIndex--;
        }
        Song song = songs.get(currentSongIndex);
        String filePath = song.getFilePath();
        play(filePath);
        updateCurrentSong(song.getName());
    }

    private void play(String filePath) {
        // Logic to play the song from the provided file path
        System.out.println("Playing song: " + filePath);
    }

    private void pause() {
        // Logic to pause the song
        System.out.println("Pausing song.");
    }

    private boolean isPaused() {
        // Logic to check if the song is currently paused
        return false;
    }

    private void updateCurrentSong(String songName) {
        System.out.println("Now Playing: " + songName);
    }

    private String getFileStoragePath() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return System.getenv("USERPROFILE") + "\\fileStorage";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return System.getProperty("user.home") + "/fileStorage";
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
    }
}