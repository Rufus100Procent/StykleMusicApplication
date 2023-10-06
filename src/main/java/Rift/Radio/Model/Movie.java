package Rift.Radio.Model;

import jakarta.persistence.*;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(nullable = false)

    public int release_year;
    @Column(nullable = false)

    public String movieName;
    @Column(nullable = false)

    public String producer;
    @Column(nullable = false)

    public String imagePath;

    public Movie() {
    }

    public Movie(int release_year, String movieName, String producer, String imagePath) {
        this.release_year = release_year;
        this.movieName = movieName;
        this.producer = producer;
        this.imagePath = imagePath;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
