package Rift.Radio;


import Rift.Radio.Model.Song;
import Rift.Radio.Repository.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SongRepositoryTest  extends ContextTest{


    @Autowired
    SongRepository songRepository;


    @Test
    void read(){



    }

    @Test
    public void testExistsBySongName_ExistingName_ShouldReturnTrue() {
        boolean exists = songRepository.existsBySongName("back in the saddle");
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsBySongName_NonExistentName_ShouldReturnFalse() {
        boolean exists = songRepository.existsBySongName("Nonexistent");
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByFilePath_ExistingPath_ShouldReturnTrue() {
        boolean exists = songRepository.existsByFilePath("./LocalStorage/MP3/Aerosmith - Back In The Saddle (Audio).mp3");
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsByFilePath_NonExistentPath_ShouldReturnFalse() {
        boolean exists = songRepository.existsByFilePath("Nonexistent path");
        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsBySongNameAndIdNot_ExistingNameWithDifferentId_ShouldReturnTrue() {
        boolean exists = songRepository.existsBySongNameAndIdNot("back in the saddle", 102L);
        assertThat(exists).isTrue();
    }

}