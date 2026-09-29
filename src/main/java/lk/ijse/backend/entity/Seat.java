package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lk.ijse.backend.util.SeatType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(nullable = false, length = 5)
    private String seatRow;

    @Column(nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatType seatType = SeatType.STANDARD;

    @JsonIgnore
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSeat> bookingSeats = new ArrayList<>();

    public Seat() {}

    public Seat(Long id, Screen screen, String seatRow, Integer seatNumber, SeatType seatType) {
        this.id = id;
        this.screen = screen;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatType = seatType != null ? seatType : SeatType.STANDARD;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Screen screen;
        private String seatRow;
        private Integer seatNumber;
        private SeatType seatType = SeatType.STANDARD;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder screen(Screen screen) { this.screen = screen; return this; }
        public Builder seatRow(String seatRow) { this.seatRow = seatRow; return this; }
        public Builder seatNumber(Integer seatNumber) { this.seatNumber = seatNumber; return this; }
        public Builder seatType(SeatType seatType) { this.seatType = seatType; return this; }

        public Seat build() {
            return new Seat(id, screen, seatRow, seatNumber, seatType);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Screen getScreen() { return screen; }
    public void setScreen(Screen screen) { this.screen = screen; }
    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }
    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }
    public List<BookingSeat> getBookingSeats() { return bookingSeats; }
    public void setBookingSeats(List<BookingSeat> bookingSeats) { this.bookingSeats = bookingSeats; }
}
