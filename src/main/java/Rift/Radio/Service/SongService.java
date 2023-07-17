package Rift.Radio.Service;

import Rift.Radio.MP3FileExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.SongNameExistsException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song uploadSong(MultipartFile file, String songName, String artistName, String album, int releaseYear, String genre) {
        // Check if song name already exists
        if (songRepository.existsBySongName(songName)) {
            throw new SongNameExistsException("Song name already exists");
        }

        try {
            validateFile(file);

            String fileName = StringUtils.cleanPath(StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "untitled.mp3");
            String storagePath = "/home/stykle/Documents/MusicApplicationBetaTesting/sample/";
            String filePath = storagePath + fileName;
            // Check if MP3 file already exists
            if (songRepository.existsByFilePath(filePath)) {
                throw new MP3FileExistsException("MP3 file already uploaded");
            }

            file.transferTo(new File(filePath));

            Song song = new Song();
            song.setSongName(songName);
            song.setArtistName(artistName);
            song.setAlbum(album);
            song.setReleaseYear(releaseYear);
            song.setGenre(genre); // Set the genre
            song.setFilePath(filePath);
            return songRepository.save(song);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload the song");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // 100 MB
        long MAX_FILE_SIZE = 104857600;
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds the limit");
        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!"mp3".equalsIgnoreCase(fileExtension)) {
            throw new RuntimeException("Invalid file format. Only MP3 files are allowed.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !MediaType.valueOf(contentType).equals(MediaType.valueOf("audio/mpeg"))) {
            throw new RuntimeException("Invalid file format. Only MP3 files are allowed.");
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

    public List<Song> getAllSongs(int page, int pageSize) {
        return songRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }

    public void deleteSong(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            String filePath = song.getFilePath();

            // Delete the song from the repository
            songRepository.delete(song);

            // Delete the MP3 file from local storage
            File mp3File = new File(filePath);
            if (mp3File.exists()) {
                mp3File.delete();
            }
        } else {
            throw new NotFoundException("Song not found");
        }
    }

    public Song editSong(Long id, MultipartFile file, String songName, String artistName, String album, int releaseYear, String genre) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            String currentFilePath = song.getFilePath();

            // Check if song name already exists
            if (songRepository.existsBySongNameAndIdNot(songName, id)) {
                throw new SongNameExistsException("Song name already exists");
            }

            // Delete the current MP3 file if a new one is uploaded
            if (file != null && !file.isEmpty()) {
                File currentFile = new File(currentFilePath);
                if (currentFile.exists()) {
                    currentFile.delete();
                }

                validateFile(file);

                String fileName = StringUtils.cleanPath(StringUtils.hasText(file.getOriginalFilename()) ?
                        file.getOriginalFilename() : "untitled.mp3");
                String storagePath = "/home/stykle/Documents/MusicApplicationBetaTesting/sample/";
                String filePath = storagePath + fileName;

                // Check if MP3 file already exists
                if (songRepository.existsByFilePath(filePath)) {
                    throw new MP3FileExistsException("MP3 file already uploaded");
                }

                try {
                    file.transferTo(new File(filePath));
                    song.setFilePath(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to upload the song");
                }
            }
            song.setSongName(songName);
            song.setArtistName(artistName);
            song.setAlbum(album);
            song.setReleaseYear(releaseYear);
            song.setGenre(genre); // Set the genre

            return songRepository.save(song);
        } else {
            throw new NotFoundException("Song not found");
        }
    }

}
