package Rift.Radio.service;

import Rift.Radio.error.ErrorType;
import Rift.Radio.error.SongException;
import Rift.Radio.modal.Song;
import Rift.Radio.repository.SongRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
public class SongService {

    private final Logger log = LoggerFactory.getLogger(SongService.class);
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song uploadSong(MultipartFile file, String songName, String artistName,
                           String album, int releaseYear, String genre) {

        log.info("Starting upload process for song '{}' by '{}'", songName, artistName);

        if (songRepository.existsBySongName(songName)) {
            log.error("Upload aborted – duplicate song name: '{}'", songName);
            throw new SongException(ErrorType.Duplicated_SONG, "Song name already exists");
        }

        try {

            validateFile(file);
            String fileName = StringUtils.cleanPath(
                    StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "untitled.mp3");
            String storagePath = "src/main/resources/LocalStorage/MP3/";
            String filePath = storagePath + fileName;
            if (songRepository.existsByFilePath(filePath)) {
                log.error("Upload aborted – file already exists at '{}'", filePath);
                throw new SongException(ErrorType.MP3_ALREADY_EXIST, "MP3 file already uploaded");
            }

            log.debug("Transferring file to '{}'", filePath);
            file.transferTo(new File(filePath));
            Song song = new Song();
            song.setSongName(songName);
            song.setArtistName(artistName);
            song.setAlbum(album);
            song.setReleaseYear(releaseYear);
            song.setGenre(genre);
            song.setFilePath(filePath);
            Song savedSong = songRepository.save(song);
            log.info("Upload successful – song '{}' saved", savedSong.getSongName());
            return savedSong;

        } catch (IOException e) {
            log.error("Upload error for song '{}': {}", songName, e.getMessage(), e);
            throw new SongException(ErrorType.FILE_NOT_FOUND, "Failed to upload the song", e);
        }
    }

    private void validateFile(MultipartFile file) {

        log.debug("Validating file '{}'", file.getOriginalFilename());
        if (file.isEmpty()) {
            log.error("Validation failed – file is empty");
            throw new SongException(ErrorType.FILE_NOT_FOUND, "File is empty");
        }

        long MAX_FILE_SIZE = 104857600;

        if (file.getSize() > MAX_FILE_SIZE) {
            log.error("Validation failed – file size {} exceeds limit", file.getSize());
            throw new SongException(ErrorType.FILE_NOT_FOUND, "File size exceeds the limit");
        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (!"mp3".equalsIgnoreCase(fileExtension)) {
            log.error("Validation failed – invalid extension: '{}'", fileExtension);
            throw new SongException(ErrorType.FILE_NOT_FOUND, "Invalid file format. Only MP3 files are allowed.");
        }

        String contentType = file.getContentType();

        if (contentType == null || !MediaType.valueOf(contentType).equals(MediaType.valueOf("audio/mpeg"))) {
            log.error("Validation failed – unsupported content type: '{}'", contentType);
            throw new SongException(ErrorType.FILE_NOT_FOUND, "Invalid file format. Only MP3 files are allowed.");
        }

    }

    public String getSongPath(Long id) {

        log.info("Retrieving file path for song ID {}", id);
        Song song = songRepository.findById(id).orElseThrow(() -> {
            log.error("Song with ID {} not found ", id);
            return new SongException(ErrorType.SONG_NOT_FOUND, "Song not found");
        });

        return song.getFilePath();

    }

    public Resource getSongFile(Long id) {

        log.info("Loading file resource for song ID {}", id);
        Song song = songRepository.findById(id).orElseThrow(() -> {
            log.error("Song with ID {} not found", id);
            return new SongException(ErrorType.SONG_NOT_FOUND, "Song not found");
        });

        try {

            Path path = Paths.get(song.getFilePath());
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                log.error("Resource missing at '{}'", song.getFilePath());
                throw new SongException(ErrorType.FILE_NOT_FOUND, "Song file not found");
            }
        } catch (IOException e) {
            log.error("I/O error for song ID {}: {}", id, e.getMessage(), e);
            throw new SongException(ErrorType.FILE_NOT_FOUND, "Song file not found", e);
        }

    }

    public List<Song> getAllSongs(int page, int pageSize) {

        List<Song> songs = songRepository.findAll(PageRequest.of(page, pageSize)).getContent();
        log.info("Fetched {} songs from page {} (page size {})", songs.size(), page, pageSize);

        return songs;

    }

    public void deleteSong(Long id) {

        log.info("Commencing deletion for song ID {}", id);
        Song song = songRepository.findById(id).orElseThrow(() -> {
            log.error("Deletion failed – song with ID {} not found", id);
            return new SongException(ErrorType.SONG_NOT_FOUND, "Song not found");
        });

        String filePath = song.getFilePath();
        songRepository.delete(song);
        log.info("Song ID {} removed from repository", id);
        File mp3File = new File(filePath);

        if (mp3File.exists()) {
            if (mp3File.delete()) {
                log.info("File '{}' deleted", filePath);
            } else {
                log.error("File deletion unsuccessful for '{}'", filePath);
            }
        } else {
            log.warn("File '{}' not found during deletion", filePath);
        }

    }

    public Song editSong(Long id, MultipartFile file, String songName, String artistName,
                         String album, int releaseYear, String genre) {

        log.info("Initiating update for song ID {}", id);
        Song song = songRepository.findById(id).orElseThrow(() -> {
            log.error("Update failed – song with ID {} not found", id);
            return new SongException(ErrorType.SONG_NOT_FOUND, "Song not found");
        });

        if (songRepository.existsBySongNameAndIdNot(songName, id)) {
            log.error("Update aborted – duplicate song name: '{}'", songName);
            throw new SongException(ErrorType.Duplicated_SONG, "Song name already exists");
        }

        if (file != null && !file.isEmpty()) {
            log.info("Processing file update for song ID {}", id);
            File oldFile = new File(song.getFilePath());
            if (oldFile.exists() && oldFile.delete()) {
                log.info("Old file '{}' removed", song.getFilePath());
            } else {
                log.warn("Old file '{}' could not be removed or did not exist", song.getFilePath());
            }
            validateFile(file);
            String fileName = StringUtils.cleanPath(
                    StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "untitled.mp3");
            String storagePath = "src/main/resources/LocalStorage/MP3/";
            String newFilePath = storagePath + fileName;

            if (songRepository.existsByFilePath(newFilePath)) {
                log.error("Update aborted – file already exists at '{}'", newFilePath);
                throw new SongException(ErrorType.MP3_ALREADY_EXIST, "MP3 file already uploaded");
            }

            try {
                log.debug("Transferring new file to '{}'", newFilePath);
                file.transferTo(new File(newFilePath));
                song.setFilePath(newFilePath);
                log.info("File updated for song ID {}: '{}'", id, newFilePath);
            } catch (IOException e) {
                log.error("File update error for song ID {}: {}", id, e.getMessage(), e);
                throw new SongException(ErrorType.FILE_NOT_FOUND, "Failed to update the song", e);
            }
        }
        song.setSongName(songName);
        song.setArtistName(artistName);
        song.setAlbum(album);
        song.setReleaseYear(releaseYear);
        song.setGenre(genre);
        Song updatedSong = songRepository.save(song);
        log.info("Song ID {} updated successfully", id);
        return updatedSong;

    }

    public void downloadSong(Long id, HttpServletResponse response) {

        log.info("Download initiated for song ID {}", id);
        Song song = songRepository.findById(id).orElseThrow(() -> {
            log.error("Download aborted – song with ID {} not found", id);
            return new SongException(ErrorType.SONG_NOT_FOUND, "Song not found");
        });
        try {

            Path path = Paths.get(song.getFilePath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists()) {
                response.setContentType("audio/mpeg");
                response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + song.getSongName() + ".mp3\"");

                log.debug("Response headers set for song '{}'", song.getSongName());
                InputStream is = resource.getInputStream();
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
                log.info("Download completed for song ID {}", id);
            } else {
                log.error("Download failed – resource missing at '{}'", song.getFilePath());
                throw new SongException(ErrorType.FILE_NOT_FOUND, "Song file not found");
            }
        } catch (IOException e) {
            log.error("Download error for song ID {}: {}", id, e.getMessage(), e);
            throw new SongException(ErrorType.FILE_NOT_FOUND, "Song file not found", e);
        }

    }

}

