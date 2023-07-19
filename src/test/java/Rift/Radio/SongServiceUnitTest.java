//package Rift.Radio;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import Rift.Radio.Model.Song;
//import Rift.Radio.Repository.SongRepository;
//import Rift.Radio.Service.SongService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.web.multipart.MultipartFile;
//import java.util.Optional;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class SongServiceUnitTest {
//
//    // Mock the SongRepository to isolate the service from the database
//    @MockBean
//    private SongRepository songRepository;
//
//    // The service to be tested
//    private SongService songService;
//
//    // Test data
//    private MultipartFile validMp3File;
//    private String songName = "Test Song";
//    private String artistName = "Test Artist";
//    private String album = "Test Album";
//    private int releaseYear = 2023;
//    private String genre = "Test Genre";
//
//    @BeforeEach
//    public void setUp() {
//        // Set up a valid MP3 file for testing
//        // You can use any other method to create a mock MultipartFile if required.
//        validMp3File = createValidMp3File();
//
//        // Create the SongService instance with the mocked SongRepository
//        songService = new SongService(songRepository);
//    }
//
//    @Test
//    public void testUploadSong_ValidData_Success() {
//        // Mock the repository's behavior for checking song name existence
//        when(songRepository.existsBySongName(anyString())).thenReturn(false);
//
//        // Mock the repository's behavior for checking file path existence
//        when(songRepository.existsByFilePath(anyString())).thenReturn(false);
//
//        // Mock the repository's behavior for saving the song
//        when(songRepository.save(any(Song.class))).thenReturn(createTestSong());
//
//        // Call the method to be tested
//        Song uploadedSong = songService.uploadSong(validMp3File, songName, artistName, album, releaseYear, genre);
//
//        // Assertions
//        assertNotNull(uploadedSong);
//        assertEquals(songName, uploadedSong.getSongName());
//        assertEquals(artistName, uploadedSong.getArtistName());
//        assertEquals(album, uploadedSong.getAlbum());
//        assertEquals(releaseYear, uploadedSong.getReleaseYear());
//        assertEquals(genre, uploadedSong.getGenre());
//        // You may add more assertions based on your requirements.
//    }
//
//    // Helper methods (you may implement these based on your test environment)
//    private MultipartFile createValidMp3File() {
//        // You can use the MockMultipartFile provided by Spring's testing utilities
//        // to create a valid MultipartFile for testing purposes.
//        // Replace the byte array with your actual MP3 file content in your test resources.
//
//        String originalFileName = "test_song.mp3";
//        String contentType = "audio/mpeg";
//        byte[] content = new byte[]{ /* Replace this with actual MP3 file content */ };
//
//        return new MockMultipartFile(originalFileName, originalFileName, contentType, content);
//    }
//
//    private Song createTestSong() {
//        // Return a test Song object for mocking the save operation
//        // You can customize this method to create a Song object with the required fields for testing.
//
//        Song song = new Song();
//        song.setId(1L); // Set a dummy ID for testing
//        song.setSongName("Test Song");
//        song.setArtistName("Test Artist");
//        song.setAlbum("Test Album");
//        song.setReleaseYear(2023);
//        song.setGenre("Test Genre");
//        song.setFilePath("/home/stykle/Documents/MusicApplicationBetaTesting/sample/test_song.mp3"); // Replace this with a valid file path
//
//        return song;
//    }
//}
