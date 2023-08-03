package Rift.Radio.SongService;

import Rift.Radio.Error.MP3FileExistsException;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import Rift.Radio.Tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:test_local.properties")
public class SongServiceIntegrationTest extends Tests {

    @Autowired
    private SongService songService;

    @MockBean
    private SongRepository songRepository;

    @BeforeEach
    public void setup() {
        // Mock the songRepository methods as needed
    }

    @Test
    public void testUploadSong_Success() throws IOException {
        Long EXISTING_SONG_ID = 1L;

        Song song = new Song();
        song.setId(EXISTING_SONG_ID);
        song.setSongName(SONG_SHOT_IN_THE_DARK.getSongName());
        song.setArtistName(SONG_SHOT_IN_THE_DARK.getArtistName());
        song.setAlbum(SONG_SHOT_IN_THE_DARK.getAlbum());
        song.setReleaseYear(SONG_SHOT_IN_THE_DARK.getReleaseYear());
        song.setGenre(SONG_SHOT_IN_THE_DARK.getGenre());
        song.setFilePath(FILE_DIRECTORY + SHOT_IN_THE_DARK_MP3);

        // Prepare test data
        MultipartFile file = createMockMultipartFile(SONG_SHOT_IN_THE_DARK.getFilePath());
        String songName = SONG_SHOT_IN_THE_DARK.getSongName();
        String artistName = SONG_SHOT_IN_THE_DARK.getArtistName();
        String album = SONG_SHOT_IN_THE_DARK.getAlbum();
        int releaseYear = SONG_SHOT_IN_THE_DARK.getReleaseYear();
        String genre = SONG_SHOT_IN_THE_DARK.getGenre();

        when(songRepository.existsBySongName(anyString())).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(false);
        when(songRepository.save(any())).thenReturn(song);

        // Perform the upload
        Song uploadedSong = songService.uploadSong(file, songName, artistName, album, releaseYear, genre);
        System.out.println(uploadedSong);

        // Assertions
        assertNotNull(uploadedSong);
        assertEquals(songName, uploadedSong.getSongName());
        assertEquals(artistName, uploadedSong.getArtistName());
        assertEquals(album, uploadedSong.getAlbum());
        assertEquals(releaseYear, uploadedSong.getReleaseYear());
        assertEquals(genre, uploadedSong.getGenre());


        // Verify interactions
        verify(songRepository, times(1)).existsBySongName(anyString());
        verify(songRepository, times(1)).existsByFilePath(anyString());
        verify(songRepository, times(1)).save(any());
    }


    @Test
    public void testUploadSong_MP3FileExistsException() throws IOException {
        // Prepare test data
        MultipartFile file = createMockMultipartFile(SONG_BACK_IN_THE_SADDLE.getFilePath());

        when(songRepository.existsBySongName(anyString())).thenReturn(false);
        when(songRepository.existsByFilePath(anyString())).thenReturn(true);

        // Perform the upload and assert the exception
        assertThrows(MP3FileExistsException.class, () -> {
            songService.uploadSong(file, SONG_BACK_IN_THE_SADDLE.getSongName(), SONG_BACK_IN_THE_SADDLE.getArtistName(),SONG_BACK_IN_THE_SADDLE.getAlbum(),
                    SONG_BACK_IN_THE_SADDLE.getReleaseYear(),SONG_BACK_IN_THE_SADDLE.getGenre());
        });

        // Verify interactions
        verify(songRepository, times(1)).existsBySongName(anyString());
        verify(songRepository, times(1)).existsByFilePath(anyString());
        verify(songRepository, never()).save(any());
    }

    @Test
    public void testUploadSong_SongNameExistsException() throws IOException {
        // Prepare test data
        MultipartFile file = createMockMultipartFile(SONG_SHOT_IN_THE_DARK.getFilePath());


        when(songRepository.existsBySongName(anyString())).thenReturn(true);

        // Perform the upload and assert the exception
        assertThrows(SongNameExistsException.class, () -> {
            songService.uploadSong(file, SONG_SHOT_IN_THE_DARK.getSongName(), SONG_SHOT_IN_THE_DARK.getArtistName(), SONG_SHOT_IN_THE_DARK.getAlbum(),
                    SONG_SHOT_IN_THE_DARK.getReleaseYear(), SONG_SHOT_IN_THE_DARK.getGenre());
        });

        // Verify interactions
        verify(songRepository, times(1)).existsBySongName(anyString());
        verify(songRepository, never()).existsByFilePath(anyString());
        verify(songRepository, never()).save(any());
    }

    // Add more test cases for other methods in SongService as needed

    private MultipartFile createMockMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] content = Files.readAllBytes(file.toPath());
        return new MockMultipartFile("file", file.getName(), "audio/mpeg", content);
    }
}
