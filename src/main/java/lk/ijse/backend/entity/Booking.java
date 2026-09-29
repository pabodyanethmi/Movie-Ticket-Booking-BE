package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lk.ijse.backend.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_reference", unique = true, length = 50)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    private LocalDateTime bookingTime = LocalDateTime.now();

    @Column(nullable = false)
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;

    public Booking() {}

    public Booking(Long id, String bookingReference, User user, Show show, LocalDateTime bookingTime, Double totalAmount, BookingStatus status, List<BookingSeat> bookingSeats) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.user = user;
        this.show = show;
        this.bookingTime = bookingTime != null ? bookingTime : LocalDateTime.now();
        this.totalAmount = totalAmount;
        this.status = status != null ? status : BookingStatus.PENDING;
        this.bookingSeats = bookingSeats != null ? bookingSeats : new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String bookingReference;
        private User user;
        private Show show;
        private LocalDateTime bookingTime = LocalDateTime.now();
        private Double totalAmount = 0.0;
        private BookingStatus status = BookingStatus.PENDING;
        private List<BookingSeat> bookingSeats = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder show(Show show) { this.show = show; return this; }
        public Builder bookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(BookingStatus status) { this.status = status; return this; }
        public Builder bookingSeats(List<BookingSeat> bookingSeats) { this.bookingSeats = bookingSeats; return this; }

        public Booking build() {
            return new Booking(id, bookingReference, user, show, bookingTime, totalAmount, status, bookingSeats);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public List<BookingSeat> getBookingSeats() { return bookingSeats; }
    public void setBookingSeats(List<BookingSeat> bookingSeats) {
        if (this.bookingSeats == null) {
            this.bookingSeats = new ArrayList<>();
        } else {
            this.bookingSeats.clear();
        }
        if (bookingSeats != null && bookingSeats != this.bookingSeats) {
            this.bookingSeats.addAll(bookingSeats);
        }
    }
    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}
