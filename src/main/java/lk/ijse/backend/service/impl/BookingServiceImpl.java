package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.BookingRequestDTO;
import lk.ijse.backend.dto.BookingResponseDTO;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.exception.SeatAlreadyBookedException;
import lk.ijse.backend.exception.UnauthorizedException;
import lk.ijse.backend.repository.*;
import lk.ijse.backend.service.BookingService;
import lk.ijse.backend.util.BookingStatus;
import lk.ijse.backend.util.PaymentMethod;
import lk.ijse.backend.util.PaymentStatus;
import lk.ijse.backend.util.SeatType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                               BookingSeatRepository bookingSeatRepository,
                               UserRepository userRepository,
                               ShowRepository showRepository,
                               SeatRepository seatRepository,
                               PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public BookingResponseDTO createBooking(Long userId, BookingRequestDTO bookingRequest) {
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId).orElse(null);
        }
        if (user == null && bookingRequest.getCustomerEmail() != null) {
            user = userRepository.findByEmail(bookingRequest.getCustomerEmail()).orElse(null);
        }
        if (user == null) {
            user = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No valid user account found in database"));
        }

        Show show = showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + bookingRequest.getShowId()));

        List<Seat> selectedSeats = seatRepository.findAllById(bookingRequest.getSeatIds());
        if (selectedSeats.size() != bookingRequest.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more selected seats were not found");
        }

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<BookingSeat> pendingSeats = bookingSeatRepository.findPendingBookingSeatsForSeats(show.getId(), bookingRequest.getSeatIds());
        for (BookingSeat bs : pendingSeats) {
            Booking b = bs.getBooking();
            if (b != null && b.getBookingTime() != null && b.getBookingTime().isBefore(cutoffTime)) {
                b.setStatus(BookingStatus.CANCELLED);
                if (b.getPayment() != null) {
                    b.getPayment().setPaymentStatus(PaymentStatus.FAILED);
                }
                bookingRepository.save(b);
            }
        }

        boolean activelyBooked = bookingSeatRepository.areAnySeatsActivelyBooked(show.getId(), bookingRequest.getSeatIds(), cutoffTime);
        if (activelyBooked) {
            throw new SeatAlreadyBookedException("One or more selected seats are already booked. Please choose other seats.");
        }

        double seatTotal = 0.0;
        List<BookingSeat> bookingSeats = new ArrayList<>();

        String bookingRef = "CX-" + (100000 + (int)(Math.random() * 900000));

        Booking booking = Booking.builder()
                .bookingReference(bookingRef)
                .user(user)
                .show(show)
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.CONFIRMED)
                .totalAmount(0.0)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        for (Seat seat : selectedSeats) {
            double price = calculateSeatPrice(show.getTicketPrice(), seat.getSeatType());
            seatTotal += price;

            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(savedBooking)
                    .seat(seat)
                    .priceAtBooking(price)
                    .build();
            bookingSeats.add(bookingSeat);
        }

        bookingSeatRepository.saveAll(bookingSeats);

        double grandTotal = (bookingRequest.getTotalAmount() != null && bookingRequest.getTotalAmount() > 0)
                ? bookingRequest.getTotalAmount()
                : seatTotal;

        savedBooking.setTotalAmount(grandTotal);
        savedBooking.setBookingSeats(bookingSeats);

        PaymentMethod pMethod = bookingRequest.getPaymentMethod() != null ? bookingRequest.getPaymentMethod() : PaymentMethod.CREDIT_CARD;
        Payment payment = Payment.builder()
                .booking(savedBooking)
                .paymentMethod(pMethod)
                .amount(grandTotal)
                .paymentStatus(PaymentStatus.COMPLETED)
                .transactionTime(LocalDateTime.now())
                .transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();

        paymentRepository.save(payment);
        savedBooking.setPayment(payment);

        return mapToDTO(savedBooking, bookingRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return mapToDTO(booking, null);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingByReference(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new ResourceNotFoundException("Booking reference cannot be empty");
        }
        String trimmedRef = reference.trim();
        Booking booking = bookingRepository.findByBookingReferenceIgnoreCase(trimmedRef)
                .orElseGet(() -> bookingRepository.findByBookingReference(trimmedRef)
                        .orElseGet(() -> {
                            try {
                                String numericPart = trimmedRef.replaceAll("\\D", "");
                                if (!numericPart.isEmpty()) {
                                    return bookingRepository.findById(Long.parseLong(numericPart)).orElse(null);
                                }
                            } catch (Exception ignored) {}
                            return null;
                        }));

        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with reference: " + reference);
        }
        return mapToDTO(booking, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserIdOrderByBookingTimeDesc(userId).stream()
                .map(b -> mapToDTO(b, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> mapToDTO(b, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDTO updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setStatus(status);
        if (status == BookingStatus.CANCELLED && booking.getPayment() != null) {
            booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
        }

        Booking updated = bookingRepository.save(booking);
        return mapToDTO(updated, null);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (!booking.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        if (booking.getPayment() != null) {
            booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
        }
        bookingRepository.save(booking);
    }

    private double calculateSeatPrice(Double basePrice, SeatType seatType) {
        if (basePrice == null) basePrice = 10.0;
        if (seatType == SeatType.VIP) {
            return basePrice * 1.5;
        } else if (seatType == SeatType.PREMIUM) {
            return basePrice * 1.25;
        }
        return basePrice;
    }

    private BookingResponseDTO mapToDTO(Booking booking, BookingRequestDTO requestDTO) {
        List<String> seatIdentifiers = booking.getBookingSeats().stream()
                .map(bs -> bs.getSeat().getSeatRow() + bs.getSeat().getSeatNumber())
                .collect(Collectors.toList());

        String txnRef = booking.getPayment() != null ? booking.getPayment().getTransactionReference() : null;
        PaymentStatus pStatus = booking.getPayment() != null ? booking.getPayment().getPaymentStatus() : PaymentStatus.PENDING;
        PaymentMethod pMethod = booking.getPayment() != null ? booking.getPayment().getPaymentMethod() : PaymentMethod.CREDIT_CARD;

        String refCode = (booking.getBookingReference() != null && !booking.getBookingReference().isEmpty())
                ? booking.getBookingReference()
                : "CX-" + (982000 + (booking.getId() != null ? booking.getId().intValue() : 100));

        String customerName = (requestDTO != null && requestDTO.getCustomerName() != null)
                ? requestDTO.getCustomerName()
                : (booking.getUser() != null ? booking.getUser().getName() : "Valued Customer");

        String customerEmail = (requestDTO != null && requestDTO.getCustomerEmail() != null)
                ? requestDTO.getCustomerEmail()
                : (booking.getUser() != null ? booking.getUser().getEmail() : "customer@scopecinemas.lk");

        String customerPhone = (requestDTO != null && requestDTO.getCustomerPhone() != null)
                ? requestDTO.getCustomerPhone()
                : (booking.getUser() != null ? booking.getUser().getPhone() : "0770000000");

        String qrPayload = String.format(
                "{\"bookingReference\":\"%s\",\"movie\":\"%s\",\"theater\":\"%s\",\"seats\":\"%s\",\"amount\":\"LKR %.2f\"}",
                refCode,
                booking.getShow().getMovie().getTitle(),
                booking.getShow().getScreen().getTheater().getName(),
                String.join(", ", seatIdentifiers),
                booking.getTotalAmount()
        );

        return BookingResponseDTO.builder()
                .id(booking.getId())
                .bookingReference(refCode)
                .userId(booking.getUser() != null ? booking.getUser().getId() : null)
                .userName(customerName)
                .userEmail(customerEmail)
                .customerPhone(customerPhone)
                .showId(booking.getShow().getId())
                .movieTitle(booking.getShow().getMovie().getTitle())
                .moviePosterUrl(booking.getShow().getMovie().getPosterUrl())
                .theaterName(booking.getShow().getScreen().getTheater().getName())
                .theaterLocation(booking.getShow().getScreen().getTheater().getLocation())
                .screenNumber(booking.getShow().getScreen().getScreenNumber())
                .showStartTime(booking.getShow().getStartTime())
                .showEndTime(booking.getShow().getEndTime())
                .seatNumbers(seatIdentifiers)
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getStatus())
                .paymentStatus(pStatus)
                .paymentMethod(pMethod)
                .transactionReference(txnRef)
                .bookingTime(booking.getBookingTime())
                .qrCodePayload(qrPayload)
                .build();
    }
}
