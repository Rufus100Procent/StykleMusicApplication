package Rift.Radio.SongService;

import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SongServiceUnitTest {
    @Mock
    private SongRepository songRepository;
    @InjectMocks
    private SongService songService;
    private final String testFilesDirectory = "src/test/java/Rift/Radio/songMP3Test/";
    @BeforeEach
    public void setUp() {
        // Initialize Mockito annotations for this test class
        MockitoAnnotations.openMocks(this);
    }
    // Test for successful song upload
    @Test
    public void testUploadSong_Success() throws IOException {
        // Mock data
        String songName = "Test Song";
        String artistName = "Test Artist";
        String album = "Test Album";
        int releaseYear = 2023;
        String genre = "Pop";
        String fileName = "Snortin’ Whiskey.mp3";

        // Load the test MP3 file
        Path mp3FilePath = Paths.get(testFilesDirectory + fileName);
        System.out.println(mp3FilePath);
        byte[] fileContent = Files.readAllBytes(mp3FilePath);
        // Debug
        System.out.println("File content size: " + fileContent.length);

        MockMultipartFile file = new MockMultipartFile("file", fileName, "audio/mpeg", fileContent);
        // Mock the repository behavior
        when(songRepository.existsBySongName(songName)).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(false);

        // Configure the songRepository.save method to return a valid Song object
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> {
            Song savedSong = invocation.getArgument(0);
            savedSong.setId(1L); // Set a valid ID (e.g., 1L) for the saved song
            return savedSong;
        });

        // Test the uploadSong method
        Song uploadedSong = songService.uploadSong(file, songName, artistName, album, releaseYear, genre);
        System.out.println("Uploaded Song: " + uploadedSong);

        // Assertions
        assertNotNull(uploadedSong);
        assertEquals(songName, uploadedSong.getSongName());
        assertEquals(artistName, uploadedSong.getArtistName());
        assertEquals(album, uploadedSong.getAlbum());
        assertEquals(releaseYear, uploadedSong.getReleaseYear());
        assertEquals(genre, uploadedSong.getGenre());
        assertNotNull(uploadedSong.getId()); // Ensure that a valid ID is set

        // Verify that the songRepository methods were called with the expected arguments
        verify(songRepository, times(1)).existsBySongName(songName);
        verify(songRepository, times(1)).existsByFilePath(anyString());
        verify(songRepository, times(1)).save(any(Song.class));
    }

    // Test for song upload with existing song name, expecting SongNameExistsException
    @Test
    public void testUploadSong_SongNameExists() throws IOException {
        // Mock data
        String songName = "Existing Song";
        String artistName = "Test Artist";
        String album = "Test Album";
        int releaseYear = 2023;
        String genre = "Pop";
        String fileName = "Aerosmith - Back In The Saddle (Audio).mp3";

        // Load the test MP3 file
        Path mp3FilePath = Paths.get(testFilesDirectory + fileName);
        System.out.println(mp3FilePath);
        byte[] fileContent = Files.readAllBytes(mp3FilePath);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "audio/mpeg", fileContent);

        // Mock the repository behavior
        when(songRepository.existsBySongName(songName)).thenReturn(true);

        // Test the uploadSong method and expect a SongNameExistsException
        assertThrows(SongNameExistsException.class,
                () -> songService.uploadSong(file, songName, artistName, album, releaseYear, genre));
    }

    // Test for successfully retrieving the song path by song ID
    @Test
    public void testGetSongPath_Success() {
        // Mock data
        Long songId = 1L;
        String filePath = testFilesDirectory + "Aerosmith - Back In The Saddle (Audio).mp3";
        Song mockSong = new Song();
        mockSong.setFilePath(filePath);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        // Test the getSongPath method
        String resultPath = songService.getSongPath(songId);
        System.out.println(resultPath);

        // Assertions
        assertEquals(filePath, resultPath);
    }

    // Test for successfully retrieving the song file resource by song ID
    @Test
    public void testGetSongFile_Success() {
        // Mock data
        Long songId = 1L;
        String filePath = testFilesDirectory + "Aerosmith - Back In The Saddle (Audio).mp3";
        Song mockSong = new Song();
        mockSong.setFilePath(filePath);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        // Test the getSongFile method
        Resource resultResource = songService.getSongFile(songId);
        System.out.println(resultResource);

        // Assertions
        assertNotNull(resultResource);
        assertEquals(UrlResource.class, resultResource.getClass());
    }

    // Test for successfully retrieving all songs with pagination
    @Test
    public void testGetAllSongs_Success() {
        // Mock data
        int page = 0;
        int pageSize = 10;
        List<Song> mockSongs = new ArrayList<>();
        for (int i = 1; i <= pageSize; i++) {
            mockSongs.add(new Song());
        }
        Page<Song> songPage = new PageImpl<>(mockSongs);

        // Mock the repository behavior
        when(songRepository.findAll(PageRequest.of(page, pageSize))).thenReturn(songPage);

        // Test the getAllSongs method
        List<Song> resultSongs = songService.getAllSongs(page, pageSize);

        // Assertions
        assertNotNull(resultSongs);
        assertEquals(pageSize, resultSongs.size());
    }

    // Test for successful song edit
    @Test
    public void testEditSong_Success() throws IOException {
        // Mock data
        Long songId = 1L;
        String songName = "Edited Song";
        String artistName = "Edited Artist";
        String album = "Edited Album";
        int releaseYear = 2024;
        String genre = "Rock";
        String fileName = "Enter Sandman (Remastered).mp3";

        // Load the test MP3 file
        Path mp3FilePath = Paths.get(testFilesDirectory + fileName);
        byte[] fileContent = Files.readAllBytes(mp3FilePath);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "audio/mpeg", fileContent);

        // Create a mock Song object to represent the existing song in the database
        Song mockSong = new Song();
        mockSong.setId(songId);
        mockSong.setSongName("Original Song");
        mockSong.setArtistName("Original Artist");
        mockSong.setAlbum("Original Album");
        mockSong.setReleaseYear(2023);
        mockSong.setGenre("Pop");
        mockSong.setFilePath(testFilesDirectory + fileName);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));
        when(songRepository.existsBySongNameAndIdNot(songName, songId)).thenReturn(false);

        // Configure the songRepository.save method to return the edited Song object
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.<Song>getArgument(0));

        // Test the editSong method
        Song editedSong = songService.editSong(songId, file, songName, artistName, album, releaseYear, genre);
        System.out.println("Edited Song: " + editedSong);

        // Assertions
        assertNotNull(editedSong);
        assertEquals(songName, editedSong.getSongName());
        assertEquals(artistName, editedSong.getArtistName());
        assertEquals(album, editedSong.getAlbum());
        assertEquals(releaseYear, editedSong.getReleaseYear());
        assertEquals(genre, editedSong.getGenre());
        assertNotNull(editedSong.getId()); // Ensure that the ID remains the same

        // Verify that the songRepository methods were called with the expected arguments
        verify(songRepository, times(1)).findById(songId);
        verify(songRepository, times(1)).existsBySongNameAndIdNot(songName, songId);
        verify(songRepository, times(1)).save(any(Song.class));
    }

    // Test for song edit with existing song name, expecting SongNameExistsException
    @Test
    public void testEditSong_SongNameExists() throws IOException {
        // Mock data
        Long songId = 1L;
        String songName = "Existing Song";
        String artistName = "Edited Artist";
        String album = "Edited Album";
        int releaseYear = 2024;
        String genre = "Rock";
        String fileName = "Enter Sandman (Remastered).mp3";

        // Load the test MP3 file
        Path mp3FilePath = Paths.get(testFilesDirectory + fileName);
        byte[] fileContent = Files.readAllBytes(mp3FilePath);
        MockMultipartFile file = new MockMultipartFile("file", fileName, "audio/mpeg", fileContent);

        // Create a mock Song object to represent the existing song in the database
        Song mockSong = new Song();
        mockSong.setId(songId);
        mockSong.setSongName("Original Song");
        mockSong.setArtistName("Original Artist");
        mockSong.setAlbum("Original Album");
        mockSong.setReleaseYear(2023);
        mockSong.setGenre("Pop");
        mockSong.setFilePath(testFilesDirectory + fileName);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));
        when(songRepository.existsBySongNameAndIdNot(songName, songId)).thenReturn(true);

        // Test the editSong method and expect a SongNameExistsException
        assertThrows(SongNameExistsException.class,
                () -> songService.editSong(songId, file, songName, artistName, album, releaseYear, genre));
    }


    // Test for successful song deletion
    @Test
    public void testDeleteSong_Success() {
        // Mock data
        Long songId = 1L;
        String filePath = testFilesDirectory + "AC DC - Hells Bells (Official Video).mp3";

        // Create a mock Song object to represent the existing song in the database
        Song mockSong = new Song();
        mockSong.setId(songId);
        mockSong.setFilePath(filePath);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        // Test the deleteSong method
        songService.deleteSong(songId);

        // Verify that the songRepository methods were called with the expected arguments
        verify(songRepository, times(1)).findById(songId);
        verify(songRepository, times(1)).delete(any(Song.class));

        // Verify that the MP3 file was deleted
        File mp3File = new File(filePath);
        assertFalse(mp3File.exists());
    }

    // Test for successful song download
    @Test
    public void testDownloadSong_Success() {
        // Mock data
        Long songId = 1L;
        String filePath = testFilesDirectory + "Enter Sandman (Remastered).mp3";

        // Create a mock Song object to represent the existing song in the database
        Song mockSong = new Song();
        mockSong.setId(songId);
        mockSong.setSongName("Test Song");
        mockSong.setFilePath(filePath);

        // Mock the repository behavior
        when(songRepository.findById(songId)).thenReturn(Optional.of(mockSong));

        // Mock the HttpServletResponse for capturing the response content
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Test the downloadSong method
        songService.downloadSong(songId, response);

        // Verify that the songRepository methods were called with the expected arguments
        verify(songRepository, times(1)).findById(songId);

        // Verify the response content type and header
        assertEquals("audio/mpeg", response.getContentType());
        assertEquals("attachment; filename=\"Test Song.mp3\"", response.getHeader("Content-Disposition"));
    }
}
