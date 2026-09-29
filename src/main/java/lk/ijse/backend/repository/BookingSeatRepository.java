package lk.ijse.backend.repository;

import lk.ijse.backend.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBookingId(Long bookingId);

    @Query("SELECT bs.seat.id FROM BookingSeat bs WHERE bs.booking.show.id = :showId AND bs.booking.status = 'CONFIRMED'")
    List<Long> findBookedSeatIdsByShowId(@Param("showId") Long showId);

    @Query("SELECT COUNT(bs) > 0 FROM BookingSeat bs WHERE bs.booking.show.id = :showId AND bs.seat.id IN :seatIds AND bs.booking.status <> 'CANCELLED'")
    boolean areAnySeatsBooked(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds);

    @Query("SELECT bs FROM BookingSeat bs WHERE bs.booking.show.id = :showId AND bs.seat.id IN :seatIds AND bs.booking.status IN ('PENDING', 'PENDING_PAYMENT')")
    List<BookingSeat> findPendingBookingSeatsForSeats(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds);

    @Query("SELECT COUNT(bs) > 0 FROM BookingSeat bs WHERE bs.booking.show.id = :showId AND bs.seat.id IN :seatIds AND (bs.booking.status = 'CONFIRMED' OR (bs.booking.status IN ('PENDING', 'PENDING_PAYMENT') AND bs.booking.bookingTime >= :cutoffTime))")
    boolean areAnySeatsActivelyBooked(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds, @Param("cutoffTime") LocalDateTime cutoffTime);
}
