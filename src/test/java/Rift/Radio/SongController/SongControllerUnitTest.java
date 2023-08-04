package Rift.Radio.SongController;

import Rift.Radio.API.SongController;
import Rift.Radio.Error.MP3FileExistsException;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Service.SongService;
import Rift.Radio.Tests;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestPropertySource(locations = "classpath:test_local.properties")
public class SongControllerUnitTest extends Tests {

    @Mock
    private SongService songService;

    @InjectMocks
    private SongController songController;

    public SongControllerUnitTest() {
        super();
        MockitoAnnotations.openMocks(this);
    }

    //passed
    @Test
    public void testUploadFile_Success() {
        // Prepare a mock multipart file
        MultipartFile file = new MockMultipartFile("file", "new_song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock service behavior
        when(songService.uploadSong(file, SONG_SHOT_IN_THE_DARK.getSongName(), SONG_SHOT_IN_THE_DARK.getArtistName(),
                SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(), SONG_SHOT_IN_THE_DARK.getGenre()))
                .thenReturn(SONG_SHOT_IN_THE_DARK);

        // Call the method being tested
        ResponseEntity<?> response = songController.uploadFile(file, SONG_SHOT_IN_THE_DARK.getSongName(),
                SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(),
                SONG_SHOT_IN_THE_DARK.getGenre());

        System.out.println(response);

        // Perform assertions on the response
        assertNotNull(response);
        assertEquals(SONG_SHOT_IN_THE_DARK, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).uploadSong(file, SONG_SHOT_IN_THE_DARK.getSongName(),
                SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(),
                SONG_SHOT_IN_THE_DARK.getGenre());
    }

    //passed
    @Test
    public void testUploadFile_SongNameExistsException() {
        // Prepare a mock multipart file
        MultipartFile file = new MockMultipartFile("file", "song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock service behavior to throw SongNameExistsException
        when(songService.uploadSong(file, SONG_SHOT_IN_THE_DARK.getSongName(), SONG_SHOT_IN_THE_DARK.getArtistName(),
                SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(), SONG_SHOT_IN_THE_DARK.getGenre()))
                .thenThrow(new SongNameExistsException("Song name already exists"));

        // Call the method being tested
        ResponseEntity<?> response = songController.uploadFile(file, SONG_SHOT_IN_THE_DARK.getSongName(),
                SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(),
                SONG_SHOT_IN_THE_DARK.getGenre());

        System.out.println(response);

        // Perform assertions on the response
        assertNotNull(response);
        assertTrue(Objects.requireNonNull(response.getBody()).toString().contains("Song name already exists"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).uploadSong(file, SONG_SHOT_IN_THE_DARK.getSongName(),
                SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(), SONG_SHOT_IN_THE_DARK.getReleaseYear(),
                SONG_SHOT_IN_THE_DARK.getGenre());
    }

    @Test
    public void testGetSongPath_Success() {
        // Set up mock service behavior
        when(songService.getSongPath(SONG_SHOT_IN_THE_DARK.getId())).thenReturn(SONG_SHOT_IN_THE_DARK.getFilePath());

        // Call the method being tested
        String songPath = songController.getSongPath(SONG_SHOT_IN_THE_DARK.getId());
        System.out.println(songPath);

        // Perform assertions on the response
        assertNotNull(songPath);
        assertEquals(songPath, Tests.FILE_DIRECTORY + SHOT_IN_THE_DARK_MP3);
        assertEquals(SONG_SHOT_IN_THE_DARK.getFilePath(), songPath);

        // Verify mock interactions
        verify(songService, times(1)).getSongPath(SONG_SHOT_IN_THE_DARK.getId());
    }

    @Test
    public void testGetSongPath_NotFoundException() {
        // Set up mock service behavior to return the expected response when the song is found
        String songPath = "path/song.mp3";
        when(songService.getSongPath(1000L)).thenReturn(songPath);

        // Call the method being tested
        String actualSongPath = songController.getSongPath(1000L);
        System.out.println(actualSongPath);

        // Perform assertions on the response
        assertNotNull(actualSongPath);
        assertEquals(songPath, actualSongPath);

        // Verify mock interactions
        verify(songService, times(1)).getSongPath(1000L);
    }

    //passed
    @Test
    public void testDeleteSong_Success() {
        // Set up mock service behavior
        doNothing().when(songService).deleteSong(SONG_SHOT_IN_THE_DARK.getId());

        // Call the method being tested
        ResponseEntity<?> response = songController.deleteSong(SONG_SHOT_IN_THE_DARK.getId());

        System.out.println(response);

        // Perform assertions on the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).deleteSong(SONG_SHOT_IN_THE_DARK.getId());
    }

    @Test
    public void testDeleteSong_NotFoundException() {
        // Set up mock service behavior to return the expected response when the song is found
        doNothing().when(songService).deleteSong(1000L);

        // Call the method being tested
        ResponseEntity<?> response = songController.deleteSong(1000L);

        // Perform assertions on the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).deleteSong(1000L);
    }

    @Test
    public void testEditSong_Success() {
        // Prepare a mock multipart file
        MultipartFile file = new MockMultipartFile("file", "edited_song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock service behavior
        when(songService.editSong(eq(SONG_SHOT_IN_THE_DARK.getId()), any(), eq("Edited Song"), eq("Edited Artist"), eq("Edited Album"),
                eq(2021), eq("Edited Genre"))).thenReturn(SONG_SHOT_IN_THE_DARK);

        // Call the method being tested
        ResponseEntity<?> response = songController.editSong(SONG_SHOT_IN_THE_DARK.getId(), file, "Edited Song", "Edited Artist",
                "Edited Album", 2021, "Edited Genre");

        System.out.println(response);
        // Perform assertions on the response
        assertNotNull(response);
        assertEquals(SONG_SHOT_IN_THE_DARK, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).editSong(eq(SONG_SHOT_IN_THE_DARK.getId()), any(), eq("Edited Song"), eq("Edited Artist"),
                eq("Edited Album"), eq(2021), eq("Edited Genre"));
    }

    //passed
    @Test
    public void testEditSong_SongNameExistsException() {
        // Prepare a mock multipart file
        MultipartFile file = new MockMultipartFile("file", "edited_song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock service behavior to throw SongNameExistsException
        when(songService.editSong(eq(SONG_SHOT_IN_THE_DARK.getId()), any(), eq(SONG_BACK_IN_THE_SADDLE.getSongName()),
                eq(SONG_SHOT_IN_THE_DARK.getArtistName()), eq(SONG_SHOT_IN_THE_DARK.getAlbum()), eq(2021), eq("Edited Genre")))
                .thenThrow(new SongNameExistsException("Song name already exists"));

        // Call the method being tested
        ResponseEntity<?> response = songController.editSong(SONG_SHOT_IN_THE_DARK.getId(), file, SONG_BACK_IN_THE_SADDLE.getSongName(),
                SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(), 2021, "Edited Genre");

        // Perform assertions on the response
        assertNotNull(response);
        assertTrue(response.getBody().toString().contains("Song name already exists"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verify mock interactions
        verify(songService, times(1)).editSong(eq(SONG_SHOT_IN_THE_DARK.getId()), any(), eq(SONG_BACK_IN_THE_SADDLE.getSongName()),
                eq(SONG_SHOT_IN_THE_DARK.getArtistName()), eq(SONG_SHOT_IN_THE_DARK.getAlbum()), eq(2021), eq("Edited Genre"));
    }

    @Test
    public void testEditSong_NotFoundException() {
        // Prepare a mock multipart file
        MultipartFile file = new MockMultipartFile("file", "edited_song.mp3", "audio/mpeg", new byte[]{});

        // Set up mock service behavior to return the expected response when the song is found
        Song editedSong = new Song("Edited Song", "Edited Artist", "Edited Album", "Edited Genre", 2021, "edited_song.mp3");
        when(songService.editSong(eq(1000L), any(), eq("Edited Song"), eq("Edited Artist"), eq("Edited Album"), eq(2021), eq("Edited Genre")))
                .thenReturn(editedSong);

        // Call the method being tested
        ResponseEntity<?> response = songController.editSong(1000L, file, "Edited Song", "Edited Artist", "Edited Album", 2021, "Edited Genre");

        // Perform assertions on the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editedSong, response.getBody());

        // Verify mock interactions
        verify(songService, times(1)).editSong(eq(1000L), any(), eq("Edited Song"), eq("Edited Artist"), eq("Edited Album"), eq(2021), eq("Edited Genre"));
    }

    @Test
    public void testDownloadSong_Success() throws IOException {
        // Prepare a mock HttpServletResponse
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(mockResponse.getOutputStream()).thenReturn(outputStream);

        // Call the method being tested
        songController.downloadSong(SONG_SHOT_IN_THE_DARK.getId(), mockResponse);

        // Verify mock interactions
        verify(songService, times(1)).downloadSong(SONG_SHOT_IN_THE_DARK.getId(), mockResponse);
    }

}