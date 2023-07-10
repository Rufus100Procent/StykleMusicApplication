package Rift.Radio.Service;


import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song uploadSong(MultipartFile file, String songName, String artistName, String album, int releaseYear) {
        // Check if song name already exists
        if (songRepository.existsBySongName(songName)) {
            throw new RuntimeException("Song name already exists");
        }

        try {
            String fileName = file.getOriginalFilename();
            String storagePath = "/home/stykle/Documents/MusicApplicationBetaTesting/sample/";
            String filePath = storagePath + fileName;

            // Check if MP3 file already exists
            if (songRepository.existsByFilePath(filePath)) {
                throw new RuntimeException("MP3 file already uploaded");
            }

            // Validate file extension
            String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
            if (!fileExtension.equalsIgnoreCase("mp3")) {
                throw new RuntimeException("Invalid file format. Only MP3 files are allowed.");
            }
            String fileMimeType = file.getContentType();
            if (!fileMimeType.equalsIgnoreCase("audio/mpeg")) {
                throw new RuntimeException("Invalid file format. Only MP3 files are allowed.");
            }

            file.transferTo(new File(filePath));

            Song song = new Song();
            song.setSongName(songName);
            song.setArtistName(artistName);
            song.setAlbum(album);
            song.setReleaseYear(releaseYear);
            song.setFilePath(filePath);
            return songRepository.save(song);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload the song");
        }
    }

    public String getSongPath(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        return songOptional.map(Song::getFilePath)
                .orElseThrow(() -> new NotFoundException("Song not found"));
    }

    public Resource getSongFile(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            try {
                Path filePath = Paths.get(song.getFilePath());
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists()) {
                    return resource;
                } else {
                    throw new NotFoundException("Song file not found");
                }
            } catch (IOException e) {
                throw new NotFoundException("Song file not found");
            }
        } else {
            throw new NotFoundException("Song not found");
        }
    }
}