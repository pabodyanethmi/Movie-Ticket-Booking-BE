package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.*;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.exception.SeatAlreadyBookedException;
import lk.ijse.backend.repository.*;
import lk.ijse.backend.service.PaymentService;
import lk.ijse.backend.util.BookingStatus;
import lk.ijse.backend.util.PaymentMethod;
import lk.ijse.backend.util.PaymentStatus;
import lk.ijse.backend.util.SeatType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @Value("${payhere.merchant.id:1211111}")
    private String merchantId;

    @Value("${payhere.merchant.secret:4MzE2MTEwMzcxMzM1MzIzOTEyNDIxMzg3MjkyNTM1MTg1MTk3NDg1}")
    private String merchantSecret;

    @Value("${payhere.currency:LKR}")
    private String defaultCurrency;

    @Value("${payhere.sandbox:true}")
    private Boolean sandbox;

    @Value("${payhere.notify.url:http://localhost:8080/api/v1/payments/payhere/notify}")
    private String notifyUrl;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                               BookingRepository bookingRepository,
                               BookingSeatRepository bookingSeatRepository,
                               UserRepository userRepository,
                               ShowRepository showRepository,
                               SeatRepository seatRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentRequest.getBookingId()));

        Payment payment = paymentRepository.findByBookingId(booking.getId())
                .orElseGet(() -> Payment.builder().booking(booking).build());

        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setTransactionTime(LocalDateTime.now());
        payment.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Payment savedPayment = paymentRepository.save(payment);

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return mapToDTO(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
        return mapToDTO(payment);
    }

    @Override
    public PaymentInitiateResponseDTO initiatePayment(PaymentInitiateRequestDTO initiateRequest) {
        String orderId = initiateRequest.getOrderId() != null && !initiateRequest.getOrderId().trim().isEmpty()
                ? initiateRequest.getOrderId()
                : "CX-" + (100000 + (int)(Math.random() * 900000));

        double amount = initiateRequest.getAmount() != null ? initiateRequest.getAmount() : 0.0;
        String formattedAmount = String.format(Locale.US, "%.2f", amount);
        String currency = initiateRequest.getCurrency() != null ? initiateRequest.getCurrency() : defaultCurrency;

        String hash = generatePayHereHash(merchantId, orderId, formattedAmount, currency, merchantSecret);

        String fullName = initiateRequest.getCustomerName() != null ? initiateRequest.getCustomerName().trim() : "Scope Guest";
        String firstName = fullName;
        String lastName = "";
        if (fullName.contains(" ")) {
            int lastSpace = fullName.lastIndexOf(" ");
            firstName = fullName.substring(0, lastSpace);
            lastName = fullName.substring(lastSpace + 1);
        }

        return PaymentInitiateResponseDTO.builder()
                .sandbox(sandbox)
                .merchantId(merchantId)
                .orderId(orderId)
                .items(initiateRequest.getItems() != null ? initiateRequest.getItems() : "Scope Cinema Booking")
                .amount(amount)
                .formattedAmount(formattedAmount)
                .currency(currency)
                .hash(hash)
                .firstName(firstName)
                .lastName(lastName)
                .email(initiateRequest.getCustomerEmail() != null ? initiateRequest.getCustomerEmail() : "guest@scopecinemas.lk")
                .phone(initiateRequest.getCustomerPhone() != null ? initiateRequest.getCustomerPhone() : "0771234567")
                .build();
    }

    @Override
    @Transactional
    public PayHereInitiateResponseDTO initiatePayHerePayment(PayHereInitiateRequestDTO request) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + request.getShowId()));

        List<Seat> selectedSeats = seatRepository.findAllById(request.getSeatIds());
        if (selectedSeats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more selected seats were not found");
        }

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(10);
        List<BookingSeat> pendingSeats = bookingSeatRepository.findPendingBookingSeatsForSeats(show.getId(), request.getSeatIds());
        for (BookingSeat bs : pendingSeats) {
            Booking b = bs.getBooking();
            if (b != null && b.getBookingTime() != null && b.getBookingTime().isBefore(cutoffTime)) {
                log.info("Auto-cancelling expired pending booking {} (created at {})", b.getId(), b.getBookingTime());
                b.setStatus(BookingStatus.CANCELLED);
                if (b.getPayment() != null) {
                    b.getPayment().setPaymentStatus(PaymentStatus.FAILED);
                }
                bookingRepository.save(b);
            }
        }

        boolean activelyBooked = bookingSeatRepository.areAnySeatsActivelyBooked(show.getId(), request.getSeatIds(), cutoffTime);
        if (activelyBooked) {
            throw new SeatAlreadyBookedException("One or more selected seats are already booked. Please choose other seats.");
        }

        User user = null;
        if (request.getCustomerEmail() != null) {
            user = userRepository.findByEmail(request.getCustomerEmail()).orElse(null);
        }
        if (user == null) {
            user = userRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No valid user account found in database"));
        }

        String bookingRef = "CX-" + (100000 + (int)(Math.random() * 900000));
        double grandTotal = request.getAmount() != null ? request.getAmount() : 1900.0;

        Booking booking = Booking.builder()
                .bookingReference(bookingRef)
                .user(user)
                .show(show)
                .bookingTime(LocalDateTime.now())
                .status(BookingStatus.PENDING_PAYMENT)
                .totalAmount(grandTotal)
                .build();

        if (booking.getBookingSeats() == null) {
            booking.setBookingSeats(new ArrayList<>());
        } else {
            booking.getBookingSeats().clear();
        }

        for (Seat seat : selectedSeats) {
            double price = calculateSeatPrice(show.getTicketPrice(), seat.getSeatType());
            BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(booking)
                    .seat(seat)
                    .priceAtBooking(price)
                    .build();
            booking.getBookingSeats().add(bookingSeat);
        }

        Booking savedBooking = bookingRepository.saveAndFlush(booking);

        Payment payment = Payment.builder()
                .booking(savedBooking)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .amount(grandTotal)
                .paymentStatus(PaymentStatus.PENDING)
                .transactionTime(LocalDateTime.now())
                .transactionReference("PENDING-" + bookingRef)
                .build();
        paymentRepository.saveAndFlush(payment);
        savedBooking.setPayment(payment);
        bookingRepository.saveAndFlush(savedBooking);

        log.info("Persisted preliminary Booking reference {} in MySQL database [status: PENDING_PAYMENT].", bookingRef);

        String formattedAmount = String.format(Locale.US, "%.2f", grandTotal);
        String currency = request.getCurrency() != null ? request.getCurrency() : defaultCurrency;

        String hash = generatePayHereHash(merchantId, bookingRef, formattedAmount, currency, merchantSecret);

        String fullName = request.getCustomerName() != null ? request.getCustomerName().trim() : "Scope Guest";
        String firstName = fullName;
        String lastName = "";
        if (fullName.contains(" ")) {
            int lastSpace = fullName.lastIndexOf(" ");
            firstName = fullName.substring(0, lastSpace);
            lastName = fullName.substring(lastSpace + 1);
        }

        return PayHereInitiateResponseDTO.builder()
                .sandbox(sandbox)
                .merchantId(merchantId)
                .orderId(bookingRef)
                .bookingReference(bookingRef)
                .items(request.getItems() != null ? request.getItems() : "Scope Cinema Ticket Booking")
                .amount(grandTotal)
                .formattedAmount(formattedAmount)
                .currency(currency)
                .hash(hash)
                .firstName(firstName)
                .lastName(lastName)
                .email(request.getCustomerEmail() != null ? request.getCustomerEmail() : "guest@scopecinemas.lk")
                .phone(request.getCustomerPhone() != null ? request.getCustomerPhone() : "0771234567")
                .notifyUrl(notifyUrl)
                .returnUrl("http://localhost:8080/pages/booking-success.html?orderId=" + bookingRef)
                .cancelUrl("http://localhost:8080/pages/checkout.html")
                .build();
    }

    @Override
    @Transactional
    public boolean processPayHereNotification(Map<String, String> params) {
        log.info("Received PayHere Notification Callback params: {}", params);

        String paramMerchantId = params.get("merchant_id");
        String orderId = params.get("order_id");
        String paymentId = params.get("payment_id");
        String payhereAmount = params.get("payhere_amount");
        String payhereCurrency = params.get("payhere_currency");
        String statusCode = params.get("status_code");
        String md5sig = params.get("md5sig");

        if (orderId == null || statusCode == null || md5sig == null) {
            log.warn("Missing critical PayHere notification fields.");
            return false;
        }

        String secretMd5 = getMd5Hash(merchantSecret).toUpperCase();
        String rawHashStr = (paramMerchantId != null ? paramMerchantId : merchantId) + orderId + payhereAmount + payhereCurrency + statusCode + secretMd5;
        String expectedHash = getMd5Hash(rawHashStr).toUpperCase();

        if (!expectedHash.equalsIgnoreCase(md5sig.trim())) {
            log.error("PayHere notification MD5 signature verification failed! Expected: {}, Received: {}", expectedHash, md5sig);
        }

        if ("2".equals(statusCode)) {
            Optional<Booking> optionalBooking = bookingRepository.findByBookingReference(orderId);
            if (optionalBooking.isEmpty()) {
                try {
                    String numericPart = orderId.replaceAll("\\D", "");
                    if (!numericPart.isEmpty()) {
                        optionalBooking = bookingRepository.findById(Long.parseLong(numericPart));
                    }
                } catch (Exception ignored) {}
            }

            if (optionalBooking.isPresent()) {
                Booking booking = optionalBooking.get();
                booking.setStatus(BookingStatus.CONFIRMED);

                Payment payment = booking.getPayment();
                if (payment == null) {
                    payment = Payment.builder().booking(booking).build();
                }
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
                payment.setTransactionReference(paymentId != null ? paymentId : "PAYHERE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                if (payhereAmount != null) {
                    try {
                        payment.setAmount(Double.parseDouble(payhereAmount));
                    } catch (Exception ignored) {}
                }
                paymentRepository.save(payment);
                booking.setPayment(payment);
                bookingRepository.saveAndFlush(booking);

                log.info("Successfully updated Booking {} [ref: {}] to CONFIRMED via PayHere Webhook.", booking.getId(), orderId);
                return true;
            } else {
                log.error("Booking with orderId / reference {} not found in database.", orderId);
            }
        }

        return false;
    }

    @Override
    @Transactional
    public BookingResponseDTO confirmPayHereOrder(String orderId) {
        Booking booking = bookingRepository.findByBookingReference(orderId)
                .orElseGet(() -> {
                    try {
                        String numericPart = orderId.replaceAll("\\D", "");
                        if (!numericPart.isEmpty()) {
                            return bookingRepository.findById(Long.parseLong(numericPart)).orElse(null);
                        }
                    } catch (Exception ignored) {}
                    return null;
                });

        if (booking == null) {
            throw new ResourceNotFoundException("Booking not found with reference: " + orderId);
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        Payment payment = booking.getPayment();
        if (payment == null) {
            payment = Payment.builder().booking(booking).build();
        }
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setTransactionTime(LocalDateTime.now());
        if (payment.getTransactionReference() == null || payment.getTransactionReference().startsWith("PENDING")) {
            payment.setTransactionReference("PAYHERE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        paymentRepository.save(payment);
        booking.setPayment(payment);

        Booking saved = bookingRepository.saveAndFlush(booking);
        log.info("Explicitly confirmed booking reference {} in MySQL database.", orderId);

        List<String> seatIdentifiers = saved.getBookingSeats().stream()
                .map(bs -> bs.getSeat().getSeatRow() + bs.getSeat().getSeatNumber())
                .collect(Collectors.toList());

        String qrPayload = String.format(
                "{\"bookingReference\":\"%s\",\"movie\":\"%s\",\"theater\":\"%s\",\"seats\":\"%s\",\"amount\":\"LKR %.2f\"}",
                saved.getBookingReference(),
                saved.getShow().getMovie().getTitle(),
                saved.getShow().getScreen().getTheater().getName(),
                String.join(", ", seatIdentifiers),
                saved.getTotalAmount()
        );

        return BookingResponseDTO.builder()
                .id(saved.getId())
                .bookingReference(saved.getBookingReference())
                .userId(saved.getUser() != null ? saved.getUser().getId() : null)
                .userName(saved.getUser() != null ? saved.getUser().getName() : "Valued Customer")
                .userEmail(saved.getUser() != null ? saved.getUser().getEmail() : "customer@scopecinemas.lk")
                .customerPhone(saved.getUser() != null ? saved.getUser().getPhone() : "0770000000")
                .showId(saved.getShow().getId())
                .movieTitle(saved.getShow().getMovie().getTitle())
                .moviePosterUrl(saved.getShow().getMovie().getPosterUrl())
                .theaterName(saved.getShow().getScreen().getTheater().getName())
                .theaterLocation(saved.getShow().getScreen().getTheater().getLocation())
                .screenNumber(saved.getShow().getScreen().getScreenNumber())
                .showStartTime(saved.getShow().getStartTime())
                .showEndTime(saved.getShow().getEndTime())
                .seatNumbers(seatIdentifiers)
                .totalAmount(saved.getTotalAmount())
                .bookingStatus(saved.getStatus())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .transactionReference(payment.getTransactionReference())
                .bookingTime(saved.getBookingTime())
                .qrCodePayload(qrPayload)
                .build();
    }

    private String generatePayHereHash(String mId, String orderId, String amountFormatted, String currency, String secret) {
        String secretHash = getMd5Hash(secret).toUpperCase();
        String rawString = mId + orderId + amountFormatted + currency + secretHash;
        return getMd5Hash(rawString).toUpperCase();
    }

    private String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
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

    private PaymentResponseDTO mapToDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .paymentMethod(payment.getPaymentMethod())
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .transactionTime(payment.getTransactionTime())
                .transactionReference(payment.getTransactionReference())
                .build();
    }
}
