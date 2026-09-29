package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "screens")
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private Integer screenNumber;

    @Column(nullable = false)
    private Integer seatCapacity = 60;

    @JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> shows = new ArrayList<>();

    public Screen() {}

    public Screen(Long id, Theater theater, Integer screenNumber, Integer seatCapacity) {
        this.id = id;
        this.theater = theater;
        this.screenNumber = screenNumber;
        this.seatCapacity = seatCapacity != null ? seatCapacity : 60;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Theater theater;
        private Integer screenNumber;
        private Integer seatCapacity = 60;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder theater(Theater theater) { this.theater = theater; return this; }
        public Builder screenNumber(Integer screenNumber) { this.screenNumber = screenNumber; return this; }
        public Builder seatCapacity(Integer seatCapacity) { this.seatCapacity = seatCapacity; return this; }

        public Screen build() {
            return new Screen(id, theater, screenNumber, seatCapacity);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Theater getTheater() { return theater; }
    public void setTheater(Theater theater) { this.theater = theater; }
    public Integer getScreenNumber() { return screenNumber; }
    public void setScreenNumber(Integer screenNumber) { this.screenNumber = screenNumber; }
    public Integer getSeatCapacity() { return seatCapacity; }
    public void setSeatCapacity(Integer seatCapacity) { this.seatCapacity = seatCapacity; }
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
    public List<Show> getShows() { return shows; }
    public void setShows(List<Show> shows) { this.shows = shows; }
}
