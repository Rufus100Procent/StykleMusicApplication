//package Rift.Radio.PlayerController;
//
//import Rift.Radio.API.PlayerController;
//import Rift.Radio.Model.Playlist;
//import Rift.Radio.Service.PlaylistService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class PlayerControllerTest {
//
//    @Mock
//    private PlaylistService playlistService;
//
//    @Mock
//    private Model model;
//
//    private PlayerController playerController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        playerController = new PlayerController(playlistService);
//    }
//
//    @Test
//    void getAllPlaylists_shouldAddPlaylistsToModelAndReturnPlaylistList() {
//        // Arrange
//        List<Playlist> playlists = new ArrayList<>();
//        playlists.add(new Playlist("Playlist 1"));
//        playlists.add(new Playlist("Playlist 2"));
//
//        when(playlistService.getAllPlaylists()).thenReturn(playlists);
//
//        // Act
//        String viewName = playerController.getAllPlaylists(model);
//
//        // Assert
//        assertEquals("playlist-list", viewName);
//        verify(model, times(1)).addAttribute("playlists", playlists);
//        verify(playlistService, times(1)).getAllPlaylists();
//    }
//
//    @Test
//    void createPlaylist_shouldCreatePlaylistAndRedirectToPlaylistList() {
//        // Arrange
//        String playlistName = "Test Playlist";
//
//        // Act
//        String viewName = playerController.createPlaylist(playlistName);
//
//        // Assert
//        assertEquals("redirect:/playlists", viewName);
//        verify(playlistService, times(1)).createPlaylist(playlistName);
//    }
//
//    @Test
//    void deletePlaylistByName_shouldDeletePlaylistAndRedirectToPlaylistList() {
//        // Arrange
//        String playlistName = "Test Playlist";
//
//        // Act
//        String viewName = playerController.deletePlaylistByName(playlistName);
//
//        // Assert
//        assertEquals("redirect:/playlists", viewName);
//        verify(playlistService, times(1)).deletePlaylistByName(playlistName);
//    }
//
//    @Test
//    void deletePlaylistById_shouldDeletePlaylistAndRedirectToPlaylistList() {
//        // Arrange
//        Long playlistId = 1L;
//
//        // Act
//        String viewName = playerController.deletePlaylistById(playlistId);
//
//        // Assert
//        assertEquals("redirect:/playlists", viewName);
//        verify(playlistService, times(1)).deletePlaylistById(playlistId);
//    }
//}
