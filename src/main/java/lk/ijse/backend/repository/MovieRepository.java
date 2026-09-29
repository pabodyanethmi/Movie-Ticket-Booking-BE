package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Movie;
import lk.ijse.backend.entity.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByReleaseDateDesc();

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByLanguageIgnoreCase(String language);

    List<Movie> findByStatus(MovieStatus status);

    List<Movie> findTop5ByBannerUrlIsNotNullOrderByRatingDesc();

    List<Movie> findByIsFeaturedTrueOrderByFeaturedOrderAsc();

    List<Movie> findTop6ByIdNotOrderByRatingDesc(Long id);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    List<Movie> findByGenreId(@Param("genreId") Long genreId);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE LOWER(g.name) = LOWER(:genreName)")
    List<Movie> findByGenreName(@Param("genreName") String genreName);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.shows s WHERE s.startTime >= CURRENT_TIMESTAMP")
    List<Movie> findNowShowingMovies();

    List<Movie> findByReleaseDateAfterOrderByReleaseDateAsc(LocalDate date);
}
