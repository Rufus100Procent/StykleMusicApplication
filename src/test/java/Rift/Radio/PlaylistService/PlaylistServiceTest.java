package Rift.Radio.PlaylistService;

import Rift.Radio.Model.Playlist;
import Rift.Radio.Repository.PlaylistRepository;
import Rift.Radio.Service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    private PlaylistService playlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playlistService = new PlaylistService(playlistRepository);
    }

    @Test
    void createPlaylist_shouldSavePlaylist() {
        // Arrange
        String playlistName = "Test Playlist";
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);

        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        // Act
        Playlist createdPlaylist = playlistService.createPlaylist(playlistName);

        // Assert
        assertEquals(playlistName, createdPlaylist.getPlaylistName());
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void getAllPlaylists_shouldReturnAllPlaylists() {
        // Arrange
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(new Playlist("Playlist 1"));
        playlists.add(new Playlist("Playlist 2"));

        when(playlistRepository.findAll()).thenReturn(playlists);

        // Act
        List<Playlist> result = playlistService.getAllPlaylists();

        // Assert
        assertEquals(playlists.size(), result.size());
        assertEquals(playlists, result);
        verify(playlistRepository, times(1)).findAll();
    }

    @Test
    void deletePlaylistByName_shouldDeletePlaylistIfExists() {
        // Arrange
        String playlistName = "Test Playlist";
        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistName);
        Optional<Playlist> playlistOptional = Optional.of(playlist);

        when(playlistRepository.findByPlaylistName(playlistName)).thenReturn(playlistOptional);

        // Act
        playlistService.deletePlaylistByName(playlistName);

        // Assert
        verify(playlistRepository, times(1)).delete(playlist);
    }

    @Test
    void deletePlaylistByName_shouldNotDeletePlaylistIfNotExists() {
        // Arrange
        String playlistName = "Non-existing Playlist";

        when(playlistRepository.findByPlaylistName(playlistName)).thenReturn(Optional.empty());

        // Act
        playlistService.deletePlaylistByName(playlistName);

        // Assert
        verify(playlistRepository, never()).delete(any(Playlist.class));
    }

    @Test
    void deletePlaylistById_shouldDeletePlaylist() {
        // Arrange
        Long playlistId = 1L;

        // Act
        playlistService.deletePlaylistById(playlistId);

        // Assert
        verify(playlistRepository, times(1)).deleteById(playlistId);
    }
}
