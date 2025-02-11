package Rift.Radio.service;

import Rift.Radio.error.MP3FileExistsException;
import Rift.Radio.error.NotFoundException;
import Rift.Radio.model.Song;
import Rift.Radio.repository.SongRepository;
import Rift.Radio.error.SongNameExistsException;
import jakarta.servlet.http.HttpServletResponse;
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
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    /**
     * Uploads a song with the provided metadata and file.
     *
     * @param file        The multipart file representing the song.
     * @param songName    The name of the song.
     * @param artistName  The name of the artist.
     * @param album       The album of the song.
     * @param releaseYear The release year of the song.
     * @param genre       The genre of the song.
     * @return The newly uploaded song entity.
     * @throws SongNameExistsException If the song name already exists in the repository.
     */
    public Song uploadSong(MultipartFile file, String songName, String artistName, String album, int releaseYear, String genre) {
        // Check if song name already exists
        if (songRepository.existsBySongName(songName)) {
            throw new SongNameExistsException("Song name already exists");
        }

        try {
            validateFile(file);

            String fileName = StringUtils.cleanPath(StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "untitled.mp3");
            String storagePath = "src/main/resources/LocalStorage/MP3/";
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
            // Handle IOException separately
            e.printStackTrace();
            throw new RuntimeException("Failed to upload the song");
        }
    }

    /**
     * Validates the uploaded file.
     *
     * @param file The multipart file to be validated.
     * @throws RuntimeException If the file is empty, exceeds the size limit, or has an invalid format.
     */
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

    /**
     * Gets the file path of the song with the given ID.
     *
     * @param id The ID of the song.
     * @return The file path of the song.
     * @throws Rift.Radio.Error.NotFoundException If the song with the given ID is not found.
     */
    public String getSongPath(Long id) {
        Optional<Song> songOptional = songRepository.findById(id);
        return songOptional.map(Song::getFilePath)
                .orElseThrow(NotFoundException::new); // Use your custom NotFoundException here
    }

    /**
     * Retrieves the song file as a Resource for the given ID.
     *
     * @param id The ID of the song.
     * @return The song file as a Resource.
     * @throws NotFoundException If the song with the given ID is not found or the file does not exist.
     */
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

    /**
     * Retrieves a paginated list of all songs.
     *
     * @param page     The page number.
     * @param pageSize The number of songs per page.
     * @return A list of songs for the given page and page size.
     */
    public List<Song> getAllSongs(int page, int pageSize) {
        return songRepository.findAll(PageRequest.of(page, pageSize)).getContent();
    }

    /**
     * Deletes the song with the given ID.
     *
     * @param id The ID of the song to be deleted.
     * @throws NotFoundException If the song with the given ID is not found.
     */
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

    /**
     * Edits the song with the given ID and updates its metadata and file if provided.
     *
     * @param id          The ID of the song to be edited.
     * @param file        The new multipart file representing the song.
     * @param songName    The new name of the song.
     * @param artistName  The new name of the artist.
     * @param album       The new album of the song.
     * @param releaseYear The new release year of the song.
     * @param genre       The new genre of the song.
     * @return The updated song entity.
     * @throws SongNameExistsException If the updated song name already exists in the repository.
     * @throws NotFoundException     If the song with the given ID is not found.
     */
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
                String storagePath = "src/main/resources/LocalStorage/MP3/";
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

    /**
     * Downloads the song with the given ID as a file to the HttpServletResponse output stream.
     *
     * @param id       The ID of the song to be downloaded.
     * @param response The HttpServletResponse object to write the file content.
     * @throws NotFoundException If the song with the given ID is not found or the file does not exist.
     */
    public void downloadSong(Long id, HttpServletResponse response) {
        Optional<Song> songOptional = songRepository.findById(id);
        if (songOptional.isPresent()) {
            Song song = songOptional.get();
            try {
                Path filePath = Paths.get(song.getFilePath());
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists()) {
                    // Set response content type for the file download
                    response.setContentType("audio/mpeg");
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + song.getSongName() + ".mp3\"");

                    // Copy the file content to the response output stream
                    InputStream inputStream = resource.getInputStream();
                    OutputStream outputStream = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.flush();
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
