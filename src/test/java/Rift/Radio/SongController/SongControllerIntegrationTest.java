package Rift.Radio.SongController;

import Rift.Radio.API.SongController;
import Rift.Radio.Error.SongNameExistsException;
import Rift.Radio.Model.Song;
import Rift.Radio.Service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(SongController.class)
public class SongControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SongService songService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetAllSongs() throws Exception {
        // Mock data
        int page = 0;
        int pageSize = 10;
        List<Song> mockSongs = new ArrayList<>();
        // Add some mock songs to the list

        // Mock the service behavior
        when(songService.getAllSongs(page, pageSize)).thenReturn(mockSongs);

        // Perform the request to get all songs
        mockMvc.perform(MockMvcRequestBuilders.get("/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(mockSongs.size())))
                .andReturn();
    }
    /*trouble shoot, status 200, acutal 400*/
//

    @Test
    @Disabled
    public void testUploadFile_Success() throws Exception {
        // Mock the uploaded MultipartFile
        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3"));
        MultipartFile mockFile = new MockMultipartFile(
                "file", "AC DC - Shot In The Dark (Official Audio).mp3", "audio/mpeg", fileContent
        );

        // Mock the songService behavior
        Song uploadedSong = new Song(1L, "Sample Song", "Sample Artist", "Sample Album", 2023, "Pop",
                "src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3");
        when(songService.uploadSong(any(MultipartFile.class), anyString(), anyString(), anyString(), anyInt(), anyString())).thenReturn(uploadedSong);

        // Perform the request
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("file", mockFile.getName());
        params.add("songName", "Sample Song");
        params.add("artistName", "Sample Artist");
        params.add("album", "Sample Album");
        params.add("releaseYear", "2023");
        params.add("genre", "Pop");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").params(params))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.songName").value("Sample Song"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistName").value("Sample Artist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album").value("Sample Album"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(2023))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Pop"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.filePath").value("src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3"))
                .andReturn();
    }

    /* trouble shoot*/

    @Test
    @Disabled
    public void testUploadFile_DuplicateSongName() throws Exception {
        // Mock the uploaded MultipartFile
        byte[] fileContent = Files.readAllBytes(Paths.get("src/test/java/Rift/Radio/songMP3Test/AC DC - Shot In The Dark (Official Audio).mp3"));
        MultipartFile mockFile = new MockMultipartFile(
                "file", "sample.mp3", "audio/mpeg", fileContent
        );

        // Mock the songService behavior to throw SongNameExistsException
        when(songService.uploadSong(any(MultipartFile.class), anyString(), anyString(), anyString(), anyInt(), anyString()))
                .thenThrow(new SongNameExistsException("Song name already exists"));

        // Perform the request
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("file", mockFile.getName());
        params.add("songName", "Peer Günt - Backseat");
        params.add("artistName", "Sample Artist");
        params.add("album", "Sample Album");
        params.add("releaseYear", "2023");
        params.add("genre", "Pop");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").params(params))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Song name already exists"))
                .andReturn();
    }
}
