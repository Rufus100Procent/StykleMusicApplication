package Rift.Radio.SongService;


import Rift.Radio.Error.NotFoundException;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import Rift.Radio.Tests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.util.Optional;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:test_local.properties")
public class SongServiceUnitTest extends Tests {
    private static final Long EXISTING_SONG_ID = 1L;
    private static final Long NON_EXISTING_SONG_ID = 100L;

    @Mock
    private HttpServletResponse response;
    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    @BeforeAll
    protected static void beforeAll() {
        Tests.beforeAll();
    }

    // MockMultipartFile representing a new song file
    private static final MockMultipartFile NEW_SONG_FILE = new MockMultipartFile(
            "file", "new_song.mp3", "audio/mpeg", new byte[]{});


    // Test for uploading a song successfully
    @Test
    public void testUploadSong_Success() throws IOException {
        // Prepare a mock multipart file
        byte[] fileContent = Files.readAllBytes(Paths.get(FILE_DIRECTORY + SHOT_IN_THE_DARK_MP3));
        MockMultipartFile file = new MockMultipartFile("file", SHOT_IN_THE_DARK_MP3, "audio/mpeg", fileContent);

        // Set up mock repository behavior
        when(songRepository.existsBySongName(any())).thenReturn(false);
        when(songRepository.existsByFilePath(any())).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenReturn(SONG_SHOT_IN_THE_DARK);

        // Call the method being tested
        Song uploadedSong = songService.uploadSong(file, "Shot in the dark", "AC DC", "Power Up", 2020, "Klassisk rock");

        // Perform assertions on the uploaded song
        assertNotNull(uploadedSong);
        assertEquals("Shot in the dark", uploadedSong.getSongName());
        assertEquals("AC DC", uploadedSong.getArtistName());
        assertEquals("Power Up", uploadedSong.getAlbum());
        assertEquals(2020, uploadedSong.getReleaseYear());
        assertEquals("Klassisk rock", uploadedSong.getGenre());
        assertEquals(FILE_DIRECTORY + SHOT_IN_THE_DARK_MP3, uploadedSong.getFilePath());

        // Verify mock interactions
        verify(songRepository, times(1)).existsBySongName(any());
        verify(songRepository, times(1)).existsByFilePath(any());
        verify(songRepository, times(1)).save(any(Song.class));
    }

    // Test for handling SongNameExistsException during song upload
    @Test
    public void testUploadSong_SongNameExistsException() {
        // Prepare a mock multipart file
        MockMultipartFile file = new MockMultipartFile("file", "song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock repository behavior
        when(songRepository.existsBySongName(any())).thenReturn(true);

        // Perform assertion for expected exception
        assertThrows(SongNameExistsException.class,
                () -> songService.uploadSong(file, "Shot in the dark", "AC DC", "Power Up", 2020, "Klassisk rock"));

        // Verify mock interactions
        verify(songRepository, times(1)).existsBySongName(any());
        verify(songRepository, never()).existsByFilePath(any());
        verify(songRepository, never()).save(any(Song.class));
    }

    // Test for getting the file path of a song successfully
    @Test
    public void testGetSongPath_NotFoundException() {
        // Set up mock repository behavior
        when(songRepository.findById(10L)).thenReturn(Optional.empty());

        // Perform assertion for expected NotFoundException
        assertThrows(NotFoundException.class, () -> songService.getSongPath(10L));

        // Verify mock interaction
        verify(songRepository, times(1)).findById(10L);
    }

    // Tests for the getSongFile method

    // Test for getting the song file successfully
    @Test
    public void testGetSongFile_Success() {
        // Set up mock repository behavior
        when(songRepository.findById(1L)).thenReturn(Optional.of(SONG_SHOT_IN_THE_DARK));

        // Call the method being tested
        Resource resource = songService.getSongFile(1L);

        // Perform assertions on the resource
        assertNotNull(resource);
        assertTrue(resource.exists());

        // Verify mock interaction
        verify(songRepository, times(1)).findById(1L);
    }

    // Test for handling NotFoundException during attempt to get song file
    @Test
    public void testGetSongFile_NotFoundException() {
        // Set up mock repository behavior
        when(songRepository.findById(10L)).thenReturn(Optional.empty());

        // Perform assertion for expected NotFoundException
        assertThrows(NotFoundException.class, () -> songService.getSongFile(10L));

        // Verify mock interaction
        verify(songRepository, times(1)).findById(10L);
    }
    // Test for editing a song successfully
    @Test
    public void testEditSong_Success() {
        // Set up existing song data and mock file
        Song existingSong = new Song();
        existingSong.setId(EXISTING_SONG_ID);
        existingSong.setSongName("Old Song Name");
        existingSong.setArtistName("Old Artist");
        existingSong.setAlbum("Old Album");
        existingSong.setReleaseYear(2000);
        existingSong.setGenre("Old Genre");
        existingSong.setFilePath("./LocalStorage/MP3/old_song.mp3");

        when(songRepository.findById(EXISTING_SONG_ID)).thenReturn(Optional.of(existingSong));
        when(songRepository.existsBySongNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(false);
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Create a new mock file for the edited song
        MockMultipartFile newFile = new MockMultipartFile(
                "file", "new_song.mp3", "audio/mpeg", "New Song Content".getBytes()
        );

        // Call the method being tested
        Song editedSong = songService.editSong(
                EXISTING_SONG_ID, newFile, "New Song Name", "New Artist", "New Album", 2022, "New Genre"
        );

        // Perform assertions on the edited song
        assertNotNull(editedSong);
        assertEquals(EXISTING_SONG_ID, editedSong.getId());
        assertEquals("New Song Name", editedSong.getSongName());
        assertEquals("New Artist", editedSong.getArtistName());
        assertEquals("New Album", editedSong.getAlbum());
        assertEquals(2022, editedSong.getReleaseYear());
        assertEquals("New Genre", editedSong.getGenre());
        assertEquals("./LocalStorage/MP3/new_song.mp3", editedSong.getFilePath());

        // Verify mock interactions
        verify(songRepository, times(1)).findById(EXISTING_SONG_ID);
        verify(songRepository, times(1)).existsBySongNameAndIdNot(anyString(), anyLong());
        verify(songRepository, times(1)).existsByFilePath(anyString());
        verify(songRepository, times(1)).save(any(Song.class));
    }


    // Test for handling NotFoundException when trying to edit a non-existing song
    @Test
    public void testEditSong_NotFoundException() {
        // Set up mock repository behavior for a non-existing song
        when(songRepository.findById(NON_EXISTING_SONG_ID)).thenReturn(Optional.empty());

        // Perform assertion for expected NotFoundException
        assertThrows(NotFoundException.class, () -> songService.editSong(
                NON_EXISTING_SONG_ID, NEW_SONG_FILE, "New Song Name", "New Artist", "New Album", 2022, "New Genre"
        ));

        // Verify mock interaction
        verify(songRepository, times(1)).findById(NON_EXISTING_SONG_ID);
    }

    // Test for handling NotFoundException when trying to download a non-existing song
    @Test
    public void testDownloadSong_NotFoundException() {
        // Set up mock repository behavior for a non-existing song
        when(songRepository.findById(NON_EXISTING_SONG_ID)).thenReturn(Optional.empty());

        // Perform assertion for expected NotFoundException
        assertThrows(NotFoundException.class, () -> songService.downloadSong(NON_EXISTING_SONG_ID, response));

        // Verify mock interaction
        verify(songRepository, times(1)).findById(NON_EXISTING_SONG_ID);
        verify(songRepository, never()).delete(any());
    }


    // Test for handling NotFoundException when trying to delete a non-existing song
    @Test
    public void testDeleteSong_NotFoundException() {
        // Set up mock repository behavior for a non-existing song
        when(songRepository.findById(NON_EXISTING_SONG_ID)).thenReturn(Optional.empty());

        // Perform assertion for expected NotFoundException
        assertThrows(NotFoundException.class, () -> songService.deleteSong(NON_EXISTING_SONG_ID));

        // Verify mock interaction
        verify(songRepository, times(1)).findById(NON_EXISTING_SONG_ID);
        verify(songRepository, never()).delete(any());
    }

    @Test
    public void testDeleteSong_Success() {
        // Set up existing song data
        Long EXISTING_SONG_ID = 1L;
        Song existingSong = new Song();
        existingSong.setId(EXISTING_SONG_ID);
        existingSong.setFilePath("src/test/java/Rift/Radio/songMP3Test/existing_song.mp3");

        when(songRepository.findById(EXISTING_SONG_ID)).thenReturn(Optional.of(existingSong));

        // Mock the songRepository.delete() method to do nothing
        doNothing().when(songRepository).delete(existingSong);

        // Call the method being tested
        assertDoesNotThrow(() -> songService.deleteSong(EXISTING_SONG_ID));

        // Verify mock interactions
        verify(songRepository).findById(EXISTING_SONG_ID);
        verify(songRepository).delete(existingSong);
    }
}

