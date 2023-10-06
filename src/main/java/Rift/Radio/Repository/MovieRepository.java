package Rift.Radio.Repository;

import Rift.Radio.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByMovieName(String movieName);

}
