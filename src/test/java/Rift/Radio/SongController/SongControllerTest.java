//package Rift.Radio.SongController;
//
//import Rift.Radio.API.SongController;
//import Rift.Radio.ContextTest;
//import Rift.Radio.Model.Song;
//import Rift.Radio.Service.SongService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//public class SongControllerTest extends ContextTest {
//
//    @Mock
//    private SongService songService;
//
//    @Mock
//    private HttpServletResponse response;
//
//
//    @InjectMocks
//    private SongController songController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetAllSongs() {
//        // Mock data
//        int page = 0;
//        int pageSize = 10;
//        List<Song> mockSongs = new ArrayList<>();
//        for (int i = 1; i <= pageSize; i++) {
//            mockSongs.add(new Song());
//        }
//
//        // Mock the service behavior
//        when(songService.getAllSongs(page, pageSize)).thenReturn(mockSongs);
//
//        // Test the getAllSongs method
//        List<Song> resultSongs = songController.getAllSongs(page, pageSize);
//
//        // Assertions
//        assertNotNull(resultSongs);
//        assertEquals(pageSize, resultSongs.size());
//    }
//
//    @Test
//    public void testUploadFile_Success() throws IOException {
//        // Mock data
//        String songName = "Test Song";
//        String artistName = "Test Artist";
//        String album = "Test Album";
//        int releaseYear = 2023;
//        String genre = "Pop";
//        String fileName = "Snortin’ Whiskey.mp3";
//
//        // Load the test MP3 file
//        Path mp3FilePath = Paths.get(TEST_FILE_DIRECTORY + fileName);
//        byte[] fileContent = Files.readAllBytes(mp3FilePath);
//        MockMultipartFile file = new MockMultipartFile("file", fileName, "audio/mpeg", fileContent);
//
//        // Create a real Song object with the necessary values
//        Song uploadedSong = new Song();
//        uploadedSong.setSongName(songName);
//        uploadedSong.setArtistName(artistName);
//        uploadedSong.setAlbum(album);
//        uploadedSong.setReleaseYear(releaseYear);
//        uploadedSong.setGenre(genre);
//
//        // Mock the service behavior
//        when(songService.uploadSong(eq(file), eq(songName), eq(artistName), eq(album), eq(releaseYear), eq(genre)))
//                .thenReturn(uploadedSong);
//
//        // Test the uploadFile method
//        ResponseEntity<?> responseEntity = songController.uploadFile(file, songName, artistName, album, releaseYear, genre);
//
//        // Assertions
//        assertNotNull(responseEntity);
//    }
//    // Add more test cases for uploadFile for existing song name and MP3 file
//
//    @Test
//    public void testGetSongPath_Success() {
//        // Mock data
//        Long songId = 1L;
//        String filePath = "/path/to/song.mp3";
//
//        // Mock the service behavior
//        when(songService.getSongPath(songId)).thenReturn(filePath);
//
//        // Test the getSongPath method
//        String resultPath = songController.getSongPath(songId);
//
//        // Assertions
//        assertEquals(filePath, resultPath);
//    }
//
//    @Test
//    public void testGetSongFile_Success() {
//        // Mock data
//        Long songId = 1L;
//        String fileName = "AC DC - Shot In The Dark (Official Audio).mp3";
//
//        // Construct the full path of the MP3 file
//        String filePath = TEST_FILE_DIRECTORY + fileName;
//
//        // Create a FileSystemResource from the MP3 file
//        Resource mockResource = new FileSystemResource(filePath);
//
//        // Mock the service behavior to return the actual MP3 file as a Resource
//        when(songService.getSongFile(songId)).thenReturn(mockResource);
//
//        // Test the getSongFile method
//        ResponseEntity<Resource> responseEntity = songController.getSongFile(songId);
//
//        // Assertions
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//
//        // Verify the response content type and header
//        String expectedContentDisposition = "attachment; filename=\"" + fileName + "\"";
//        System.out.println(expectedContentDisposition);
//        String actualContentDisposition = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
//        assertEquals(expectedContentDisposition, actualContentDisposition);
//    }
//
//    // Add test cases for getSongFile when song is not found
//
//    @Test
//    public void testDeleteSong_Success() {
//        // Mock data
//        Long songId = 1L;
//
//        // Test the deleteSong method
//        ResponseEntity<?> responseEntity = songController.deleteSong(songId);
//
//        // Assertions
//        assertNotNull(responseEntity);
//        assertEquals(ResponseEntity.ok().build(), responseEntity);
//    }
//
//    // Add test cases for deleteSong when song is not found
//
//    @Test
//    public void testEditSong_Success()  {
//        // Mock data
//        Long songId = 1L;
//        String songName = "Edited Song";
//        String artistName = "Edited Artist";
//        String album = "Edited Album";
//        int releaseYear = 2022;
//        String genre = "Rock";
//
//        // Create the expected edited song object with the mock data
//        Song expectedEditedSong = new Song();
//        expectedEditedSong.setId(songId);
//        expectedEditedSong.setSongName(songName);
//        expectedEditedSong.setArtistName(artistName);
//        expectedEditedSong.setAlbum(album);
//        expectedEditedSong.setReleaseYear(releaseYear);
//        expectedEditedSong.setGenre(genre);
//
//        // Mock the service behavior to return the expected edited song
//        when(songService.editSong(eq(songId), any(), eq(songName), eq(artistName), eq(album), eq(releaseYear), eq(genre)))
//                .thenReturn(expectedEditedSong);
//
//        // Test the editSong method in the SongController
//        ResponseEntity<Song> responseEntity = (ResponseEntity<Song>) songController.editSong(songId, null, songName, artistName, album, releaseYear, genre);
//
//        // Assertions
//        // Check if the response status is HttpStatus.OK (200)
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        // Check if the response body (edited song) matches the expected edited song
//        assertEquals(expectedEditedSong, responseEntity.getBody());
//    }
//
//
//    @Test
//    public void testDownloadSong_Success() {
//        // Mock data
//        Long songId = 1L;
//
//        // Test the downloadSong method
//        songController.downloadSong(songId, response);
//
//        // Verify that the service method was called with the expected arguments
//        verify(songService, times(1)).downloadSong(songId, response);
//    }
//
//}