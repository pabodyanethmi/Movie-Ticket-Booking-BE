package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "booking_seats")
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private Double priceAtBooking;

    public BookingSeat() {}

    public BookingSeat(Long id, Booking booking, Seat seat, Double priceAtBooking) {
        this.id = id;
        this.booking = booking;
        this.seat = seat;
        this.priceAtBooking = priceAtBooking;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Booking booking;
        private Seat seat;
        private Double priceAtBooking;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder booking(Booking booking) { this.booking = booking; return this; }
        public Builder seat(Seat seat) { this.seat = seat; return this; }
        public Builder priceAtBooking(Double priceAtBooking) { this.priceAtBooking = priceAtBooking; return this; }

        public BookingSeat build() {
            return new BookingSeat(id, booking, seat, priceAtBooking);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }
    public Double getPriceAtBooking() { return priceAtBooking; }
    public void setPriceAtBooking(Double priceAtBooking) { this.priceAtBooking = priceAtBooking; }
}
