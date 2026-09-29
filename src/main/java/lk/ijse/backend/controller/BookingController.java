package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.BookingRequestDTO;
import lk.ijse.backend.dto.BookingResponseDTO;
import lk.ijse.backend.dto.UserDTO;
import lk.ijse.backend.service.BookingService;
import lk.ijse.backend.service.UserService;
import lk.ijse.backend.util.BookingStatus;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<StandardResponse<BookingResponseDTO>> createBooking(
            Authentication authentication,
            @Valid @RequestBody BookingRequestDTO bookingRequest) {
        Long userId = extractUserIdSafely(authentication, bookingRequest.getCustomerEmail());
        BookingResponseDTO response = bookingService.createBooking(userId, bookingRequest);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Booking confirmed successfully", response),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/confirm")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> confirmBooking(
            Authentication authentication,
            @Valid @RequestBody BookingRequestDTO bookingRequest) {
        Long userId = extractUserIdSafely(authentication, bookingRequest.getCustomerEmail());
        BookingResponseDTO response = bookingService.createBooking(userId, bookingRequest);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Booking confirmed successfully", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<StandardResponse<List<BookingResponseDTO>>> getMyBookings(Authentication authentication) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(user.getId());
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "User bookings fetched successfully", bookings)
        );
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Booking details fetched successfully", booking)
        );
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> getBookingByReference(@PathVariable("reference") String reference) {
        BookingResponseDTO booking = bookingService.getBookingByReference(reference);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Booking details fetched by reference", booking)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> getBookingByOrderId(@PathVariable("orderId") String orderId) {
        BookingResponseDTO booking = bookingService.getBookingByReference(orderId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Booking details fetched by order reference", booking)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<List<BookingResponseDTO>>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "All bookings fetched successfully", bookings)
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<BookingResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        BookingResponseDTO updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Booking status updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<Void>> cancelBooking(
            Authentication authentication,
            @PathVariable Long id) {
        UserDTO user = userService.getUserByEmail(authentication.getName());
        bookingService.cancelBooking(id, user.getId());
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Booking cancelled successfully", null)
        );
    }

    private Long extractUserIdSafely(Authentication authentication, String customerEmail) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            try {
                UserDTO user = userService.getUserByEmail(authentication.getName());
                if (user != null) return user.getId();
            } catch (Exception ignored) {}
        }
        if (customerEmail != null && !customerEmail.trim().isEmpty()) {
            try {
                UserDTO user = userService.getUserByEmail(customerEmail.trim());
                if (user != null) return user.getId();
            } catch (Exception ignored) {}
        }
        return null;
    }
}
