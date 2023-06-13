package AWS.File.hosting.Service;

import org.springframework.stereotype.Service;

@Service
public class MusicPlayerService {
    private boolean isPlaying;

    public void play() {
        // Implement the logic to play audio
        isPlaying = true;
        System.out.println("Playing audio");
    }

    public void pause() {
        // Implement the logic to pause audio
        isPlaying = false;
        System.out.println("Pausing audio");
    }

    public void next() {
        // Implement the logic to skip to the next audio track
        System.out.println("Skipping to next track");
    }

    public void previous() {
        // Implement the logic to skip to the previous audio track
        System.out.println("Skipping to previous track");
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
