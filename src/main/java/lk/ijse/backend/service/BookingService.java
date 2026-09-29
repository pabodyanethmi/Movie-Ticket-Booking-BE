package lk.ijse.backend.service;

import lk.ijse.backend.dto.BookingRequestDTO;
import lk.ijse.backend.dto.BookingResponseDTO;
import lk.ijse.backend.util.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(Long userId, BookingRequestDTO bookingRequest);
    BookingResponseDTO getBookingById(Long id);
    BookingResponseDTO getBookingByReference(String reference);
    List<BookingResponseDTO> getBookingsByUserId(Long userId);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO updateBookingStatus(Long id, BookingStatus status);
    void cancelBooking(Long id, Long userId);
}
