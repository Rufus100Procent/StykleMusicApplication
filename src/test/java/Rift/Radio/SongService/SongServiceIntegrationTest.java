package Rift.Radio.SongService;

import Rift.Radio.Error.MP3FileExistsException;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SongServiceIntegrationTest {

    @Autowired
    private SongService songService;

    @MockBean
    private SongRepository songRepository;

    // Other mock beans can be added if needed

    @BeforeEach
    public void setUp() {
        // Setup any necessary mock behaviors here
    }


    @Test
    public void testUploadSong_Success() throws IOException {
        // Mock data
        String songName = "Test Song";
        String artistName = "Test Artist";
        String album = "Test Album";
        int releaseYear = 2023;
        String genre = "Pop";

        // Read the content of the sample MP3 file into a byte array
        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3"));

        // Create a mock MultipartFile with the sample MP3 file content
        MultipartFile mockFile = new MockMultipartFile(
                "file",             // The parameter name used in the controller
                "sample.mp3",       // The original filename
                "audio/mpeg",       // The content type
                fileContent         // The file content as byte[]
        );

        // Mock the repository behavior
        when(songRepository.existsBySongName(songName)).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(new Song());

        // Perform the uploadSong method
        Song uploadedSong = songService.uploadSong(mockFile, songName, artistName, album, releaseYear, genre);

        // Assertions
        assertNotNull(uploadedSong);
    }

    @Test
    public void testUploadSong_SongNameExists() {
        // Mock data
        String songName = "Existing Song";
        String artistName = "Test Artist";
        String album = "Test Album";
        int releaseYear = 2023;
        String genre = "Pop";

        // Create a mock MultipartFile with a dummy MP3 file content
        MultipartFile mockFile = new MockMultipartFile(
                "file",             // The parameter name used in the controller
                "dummy.mp3",        // The original filename
                "audio/mpeg",       // The content type
                new byte[0]         // Empty file content
        );

        // Mock the repository behavior
        when(songRepository.existsBySongName(songName)).thenReturn(true);

        // Perform the uploadSong method and assert that it throws SongNameExistsException
        assertThrows(SongNameExistsException.class, () -> songService.uploadSong(mockFile, songName, artistName, album, releaseYear, genre));

        // Verify that the repository methods were called appropriately
        verify(songRepository, times(1)).existsBySongName(songName);
        verify(songRepository, never()).existsByFilePath(anyString());
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    public void testUploadSong_MP3FileExists() throws IOException {
        // Mock data
        String songName = "Test Song";
        String artistName = "Test Artist";
        String album = "Test Album";
        int releaseYear = 2023;
        String genre = "Pop";

        // Read the content of the real MP3 file into a byte array
        Path mp3FilePath = Paths.get(ResourceUtils.getFile("src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3").getAbsolutePath());
        byte[] fileContent = Files.readAllBytes(mp3FilePath);

        // Create a mock MultipartFile with the real MP3 file content
        MultipartFile mockFile = new MockMultipartFile(
                "file",                 // The parameter name used in the controller
                "real_sample.mp3",      // The original filename
                "audio/mpeg",           // The content type
                fileContent             // The file content as byte[]
        );

        // Mock the repository behavior
        when(songRepository.existsBySongName(songName)).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(true);

        // Perform the uploadSong method and assert that it throws an MP3FileExistsException
        assertThrows(MP3FileExistsException.class, () -> songService.uploadSong(mockFile, songName, artistName, album, releaseYear, genre));

        // Verify that the repository methods were called appropriately
        verify(songRepository, times(1)).existsBySongName(songName);
        verify(songRepository, times(1)).existsByFilePath(anyString());
        verify(songRepository, never()).save(any(Song.class));
    }

}
