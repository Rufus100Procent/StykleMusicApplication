package Rift.Radio.Service;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.PlaylistRepository;
import Rift.Radio.Repository.SongRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public Playlist createPlaylist(String playlistName, Long songId) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        playlist.setSongId(songId);
        return playlistRepository.save(playlist);
    }

    public void updatePlaylist(Long playlistId, String playlistName, MultipartFile imageFile) throws IOException {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();
            playlist.setPlaylistName(playlistName);

            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete the existing image file
                deleteImage(playlist.getImagePath());

                // Save the new image and get the path
                String imagePath = saveImage(imageFile);
                playlist.setImagePath(imagePath);
            }

            playlistRepository.save(playlist);
        }
    }

    public void deletePlaylist(Long playlistId) {
        Optional<Playlist> playlistOptional = playlistRepository.findById(playlistId);
        if (playlistOptional.isPresent()) {
            Playlist playlist = playlistOptional.get();

            // Delete the image file associated with the playlist
            deleteImage(playlist.getImagePath());

            playlistRepository.delete(playlist);
        }
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        // Specify the path for image storage
        String IMAGE_UPLOAD_DIR = "/home/stykle/Documents/MusicApplicationBetaTesting/sample/";
        String filePath = IMAGE_UPLOAD_DIR + fileName;
        Path destinationPath = new File(filePath).toPath();

        Files.copy(imageFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                boolean deleteResult = imageFile.delete();
                if (!deleteResult) {
                    // Handle the case when the file deletion fails
                    // You can throw an exception, log an error, or perform any other necessary action
                    throw new RuntimeException("Failed to delete playlist image file: " + imagePath);
                }
            }
        }
    }
}