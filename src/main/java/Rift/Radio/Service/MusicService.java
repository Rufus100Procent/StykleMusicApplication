//// MusicService.java
//package Rift.Radio.Service;
//
//import Rift.Radio.Model.Album;
//import Rift.Radio.Model.Artist;
//import Rift.Radio.Repository.AlbumRepository;
//import Rift.Radio.Repository.ArtistRepository;
//import Rift.Radio.Repository.SongRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Service;
//
//import Rift.Radio.Model.Song;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class MusicService {
//
//    private final SongRepository songRepository;
//    private final ArtistRepository artistRepository;
//    private final AlbumRepository albumRepository;
//
//    @Autowired
//    public MusicService(SongRepository songRepository, ArtistRepository artistRepository, AlbumRepository albumRepository) {
//        this.songRepository = songRepository;
//        this.artistRepository = artistRepository;
//        this.albumRepository = albumRepository;
//    }
//
//    /**
//     * Uploads a song to the server and saves its information in the database.
//     *
//     * @param file        The MultipartFile representing the song file.
//     * @param songName    The name of the song.
//     * @param artistName  The name of the artist.
//     * @param albumName   The name of the album.
//     * @param releaseYear The release year of the song.
//     * @throws IOException If an error occurs during file operations.
//     */
//    public void uploadSong(MultipartFile file, String songName, String artistName, String albumName, int releaseYear) throws IOException {
//        Artist artist = getOrCreateArtist(artistName);
//        Song song = new Song();
//        song.setSongName(songName);
//        song.setArtist(artist);
//        song.setReleaseYear(releaseYear);
//
//        Album album = saveAlbum(albumName); // Save the album and get the persisted instance
//        song.setAlbum(album); // Associate the album with the song
//
//        saveSongFile(file, song);
//
//        songRepository.save(song);
//    }
//    /**
//     * Retrieves the path of a song file based on its name and allows frontend to download it.
//     *
//     * @param songName The name of the song.
//     * @return The resource representing the song file.
//     * @throws IOException If the song is not found or an error occurs during file operations.
//     */
//    public Resource downloadSong(String songName) throws IOException {
//        Optional<Song> songOptional = songRepository.findBySongName(songName);
//        if (songOptional.isPresent()) {
//            String filePath = songOptional.get().getFilePath();
//            File file = new File(filePath);
//            if (file.exists()) {
//                return new FileSystemResource(file);
//            } else {
//                throw new IOException("Song file not found");
//            }
//        }
//        throw new IOException("Song not found");
//    }
//
//    /**
//     * Fetches a list of songs with their details (song name, artist name, album name, and release year).
//     *
//     * @return The list of songs with details.
//     */
//    public List<Song> getSongsWithDetails() {
//        List<Object[]> songsWithDetails = songRepository.findAllWithDetails();
//        return songsWithDetails.stream()
//                .map(this::mapToSongWithDetails)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Maps the result of the query to a Song object with the specified fields.
//     *
//     * @param result The result array from the query.
//     * @return The Song object with details.
//     */
//    private Song mapToSongWithDetails(Object[] result) {
//        Song song = new Song();
//        song.setSongName((String) result[0]);
//        Artist artist = new Artist();
//        artist.setArtistName((String) result[1]);
//        song.setArtist(artist);
//        Album album = new Album();
//        album.setAlbumName((String) result[2]);
//        song.setAlbum(album);
//        song.setReleaseYear((int) result[3]);
//        return song;
//    }
//
//    /**
//     * Retrieves an existing artist from the database or creates a new one if not found.
//     *
//     * @param artistName The name of the artist.
//     * @return The existing or newly created Artist object.
//     */
//    private Artist getOrCreateArtist(String artistName) {
//        if (artistName != null && !artistName.isEmpty()) {
//            Optional<Artist> artistOptional = artistRepository.findByArtistName(artistName);
//            return artistOptional.orElseGet(() -> {
//                Artist artist = new Artist();
//                artist.setArtistName(artistName);
//                return artistRepository.save(artist);
//            });
//        }
//        // Handle the case when artistName is null or empty
//        throw new IllegalArgumentException("Artist name cannot be null or empty");
//    }
//
//
//    /**
//     * Saves the uploaded song file to the server's storage and sets its file path.
//     *
//     * @param file The MultipartFile representing the song file.
//     * @param song The Song object associated with the file.
//     * @throws IOException If an error occurs during file operations.
//     */
//    private void saveSongFile(MultipartFile file, Song song) throws IOException {
//        String fileName = file.getOriginalFilename();
//        String FOLDER_PATH = "/home/stykle/Desktop/Songs/";
//        String filePath = FOLDER_PATH + File.separator + fileName;
//        Path destination = Path.of(filePath);
//        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//        song.setFilePath(filePath);
//    }
//
//    /**
//     * Creates a new album with the given name and associates it with the```java
//     */
//    private Album saveAlbum(String albumName) {
//        if (albumName != null && !albumName.isEmpty()) {
//            Album album = new Album();
//            album.setAlbumName(albumName);
//            return albumRepository.save(album);
//        }
//        throw new IllegalArgumentException("Album name cannot be null or empty");
//    }
//
//    /**
//     * Retrieves a list of all songs from the database.
//     *
//     * @return The list of songs.
//     */
//    public List<Song> getAllSongs() {
//        return songRepository.findAll();
//    }
//}