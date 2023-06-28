package AWS.File.hosting.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    private final List<String> playlists = new ArrayList<>();

    public List<String> getAllPlaylists() {
        return playlists;
    }

    public boolean createPlaylist(String playlistName) {
        if (playlists.stream().anyMatch(p -> p.equalsIgnoreCase(playlistName.trim()))) {
            return false;
        } else {
            playlists.add(playlistName.trim());
            return true;
        }
    }

    public void deletePlaylists(List<String> selectedPlaylists) {
        if (selectedPlaylists != null) {
            playlists.removeAll(selectedPlaylists);
        }
    }
}
