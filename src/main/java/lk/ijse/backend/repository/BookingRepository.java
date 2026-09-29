package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Booking;
import lk.ijse.backend.util.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookingTimeDesc(Long userId);
    List<Booking> findByShowId(Long showId);
    List<Booking> findTop10ByOrderByBookingTimeDesc();
    long countByStatus(BookingStatus status);
    Optional<Booking> findByBookingReference(String bookingReference);
    Optional<Booking> findByBookingReferenceIgnoreCase(String bookingReference);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0.0) FROM Booking b WHERE b.status = 'CONFIRMED'")
    Double calculateTotalRevenue();
}
