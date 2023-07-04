package AWS.File.hosting;

import AWS.File.hosting.Model.Artist;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Repo extends JpaRepository<Artist, Long> {
    @Query(value = "CREATE TABLE IF NOT EXISTS artist (id SERIAL PRIMARY KEY, name VARCHAR(255))", nativeQuery = true)
    void createArtistTable();

    @Query(value = "INSERT INTO songs (song_name) VALUES (?1)", nativeQuery = true)
    @Modifying
    void insertArtist(String name);
}
