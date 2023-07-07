package Rift.Radio.Service;

import Rift.Radio.Repository.SongRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import Rift.Radio.Model.Song;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    private final SongRepository songRepository;

    public MusicService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public String uploadSong(MultipartFile file, String songName, String artist, int releaseYear, String album) throws IOException {
        String folderPath = "/home/stykle/Downloads/LearningJPA/src/main/resources/static/songs/";
        String filePath = folderPath + file.getOriginalFilename();

        // Save the file path to the database
        Song song = new Song();
        song.setSongName(songName);
        song.setArtist(artist);
        song.setReleaseYear(releaseYear);
        song.setAlbum(album);
        song.setFilePath(filePath);
        songRepository.save(song);

        // Transfer the uploaded file to the designated folder
        File destFile = new File(filePath);
        file.transferTo(destFile);

        return filePath;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Optional<Song> getSongById(Long id) {
        return songRepository.findById(id);
    }

    public Resource loadSongResource(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Failed to load the song file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to load the song file.", e);
        }
    }
}