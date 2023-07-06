package AWS.File.hosting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SongDataLoader {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/stykledb";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "root";

    public static void main(String[] args) {
        String[] songNames = {
                "AC-DC - Fire-Your-Guns.mp3",
                "AC DC - Shoot To Thrill.mp3",
                // Add other song names here
        };

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)) {
            insertSongs(connection, songNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertSongs(Connection connection, String[] songNames) throws SQLException {
        String insertArtistQuery = "INSERT INTO Artist (name) VALUES (?)";
        String insertTrackQuery = "INSERT INTO Track (name, release_year, artist_id) VALUES (?, ?, ?)";

        try (PreparedStatement insertArtistStmt = connection.prepareStatement(insertArtistQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTrackStmt = connection.prepareStatement(insertTrackQuery)) {

            for (String songName : songNames) {
                String artistName = extractArtistName(songName);
                String trackName = extractTrackName(songName);
                int releaseYear = extractReleaseYear(songName);

                int artistId = insertArtist(connection, insertArtistStmt, artistName);
                insertTrack(insertTrackStmt, trackName, releaseYear, artistId);
            }
        }
    }

    private static int insertArtist(Connection connection, PreparedStatement insertArtistStmt, String artistName) throws SQLException {
        insertArtistStmt.setString(1, artistName);
        insertArtistStmt.executeUpdate();

        int artistId;
        try (var generatedKeys = insertArtistStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                artistId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating artist failed, no ID obtained.");
            }
        }
        return artistId;
    }

    private static void insertTrack(PreparedStatement insertTrackStmt, String trackName, int releaseYear, int artistId) throws SQLException {
        insertTrackStmt.setString(1, trackName);
        insertTrackStmt.setInt(2, releaseYear);
        insertTrackStmt.setInt(3, artistId);
        insertTrackStmt.executeUpdate();
    }

    private static String extractArtistName(String songName) {
        // Extract the artist name from the song name
        return songName.split(" - ")[0];
    }

    private static String extractTrackName(String songName) {
        // Extract the track name from the song name
        return songName.substring(songName.lastIndexOf("/") + 1);
    }

    private static int extractReleaseYear(String songName) {
        // Extract the release year from the song name
        // This is just a placeholder, you need to implement your logic to extract the year from the song name
        return 0;
    }
}
