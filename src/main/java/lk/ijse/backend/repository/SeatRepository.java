package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenIdOrderBySeatRowAscSeatNumberAsc(Long screenId);
    Optional<Seat> findByScreenIdAndSeatRowAndSeatNumber(Long screenId, String seatRow, Integer seatNumber);
    long countByScreenId(Long screenId);
}
