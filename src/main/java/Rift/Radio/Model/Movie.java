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

    public String movie_Name;
    @Column(nullable = false)

    public String imagePath;

    public Movie(int release_year, String movie_Name, String imagePath) {
        this.release_year = release_year;
        this.movie_Name = movie_Name;
        this.imagePath = imagePath;
    }

    public Movie() {

    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getMovie_Name() {
        return movie_Name;
    }

    public void setMovie_Name(String movie_Name) {
        this.movie_Name = movie_Name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
