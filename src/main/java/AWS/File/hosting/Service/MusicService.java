package AWS.File.hosting.Service;

import org.springframework.stereotype.Service;

import AWS.File.hosting.Model.Song;
import AWS.File.hosting.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class MusicService {

    private final SongRepository songRepository;

    private final String FOLDER_PATH = "/home/stykle/Downloads/LearningJPA/src/main/resources/static/songs/";


    public MusicService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public String uploadSong(MultipartFile file, String songName, String artist, int releaseYear, String album) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        // Save the file path to the database
        Song song = new Song();
        song.setTitle(songName);
        song.setArtist(artist);
        song.setReleaseYear(releaseYear);
        song.setAlbum(album);
        song.setFilePath(filePath);
        Song savedSong = songRepository.save(song);

        // Transfer the uploaded file to the designated folder
        file.transferTo(new File(filePath));

        return filePath;
    }

    public byte[] downloadSong(String fileName) throws IOException {
        Optional<Song> song = songRepository.findByTitle(fileName);
        String filePath = song.get().getFilePath();
        byte[] mp3Data = Files.readAllBytes(new File(filePath).toPath());
        return mp3Data;
    }
}
