package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovieId(Long movieId);
    List<Show> findByScreenId(Long screenId);
    List<Show> findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(Long movieId, LocalDateTime time);
    List<Show> findByStartTimeBetweenOrderByStartTimeAsc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Show s WHERE s.screen.id = :screenId AND " +
           "((s.startTime <= :endTime AND s.endTime >= :startTime))")
    List<Show> findConflictingShows(@Param("screenId") Long screenId,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(s) > 0 FROM Show s WHERE s.screen.theater.id = :cinemaId AND s.movie.id = :movieId AND s.startTime = :startTime")
    boolean existsShowSlot(
        @Param("cinemaId") Long cinemaId,
        @Param("movieId") Long movieId,
        @Param("startTime") LocalDateTime startTime
    );
}
