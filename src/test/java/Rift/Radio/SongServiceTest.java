package Rift.Radio;

import Rift.Radio.Error.MP3FileExistsException;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import Rift.Radio.Service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SongServiceTest {

        @Mock
        private SongRepository songRepository;

        @InjectMocks
        private SongService songService;

        private String fileMp3Path;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);

            // Specify the full path of the actual MP3 file for testing
            fileMp3Path = "/home/stykle/Documents/MusicApplicationBetaTesting/src/test/java/Rift/Radio/songMP3Test/AC DC - Shoot To Thrill.mp3";
        }

        @Test
        public void testUploadSong_Success() throws IOException {
            // Prepare the real MP3 file for testing
            File mp3File = new File(fileMp3Path);

            // Check if the MP3 file exists at the specified path
            if (!mp3File.exists()) {
                throw new FileNotFoundException("MP3 file not found at the specified path: " + fileMp3Path);
            }

            // Create the MockMultipartFile for testing
            MultipartFile realMP3File = new MockMultipartFile(
                    mp3File.getName(),
                    mp3File.getName(),
                    "audio/mpeg",
                    mp3File.toURI().toURL().openStream()
            );

            String songName = "My Song";
            String artistName = "Artist";
            String album = "Album";
            int releaseYear = 2020;
            String genre = "Rock";
            when(songRepository.existsBySongName(songName)).thenReturn(false);
            when(songRepository.existsByFilePath(any())).thenReturn(false);

            // Call the service method
            Song uploadedSong = songService.uploadSong(realMP3File, songName, artistName, album, releaseYear, genre);
            System.out.println("Real MP3 File: " + realMP3File);
            assertNotNull(uploadedSong);
            assertEquals(songName, uploadedSong.getSongName());
            assertEquals(artistName, uploadedSong.getArtistName());
            assertEquals(album, uploadedSong.getAlbum());
            assertEquals(releaseYear, uploadedSong.getReleaseYear());
            assertEquals(genre, uploadedSong.getGenre());
        }

        @Test
        public void testUploadSong_SongNameExists() throws IOException {
            String existingSongName = "Existing Song";
            when(songRepository.existsBySongName(existingSongName)).thenReturn(true);

            // Mock a real MP3 file for testing (you can use a different MP3 file for this test)
            File mp3File = new File(fileMp3Path);
            MultipartFile realMP3File = new MockMultipartFile("existing_song.mp3", mp3File.getName(), "audio/mpeg", mp3File.toURI().toURL().openStream());

            // Call the service method and assert the exception is thrown
            assertThrows(SongNameExistsException.class, () -> {
                songService.uploadSong(realMP3File, existingSongName, "Artist", "Album", 2023, "Genre");
            });
        }

}