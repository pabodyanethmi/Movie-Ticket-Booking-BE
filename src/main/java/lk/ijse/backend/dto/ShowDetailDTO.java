package lk.ijse.backend.dto;

import java.util.List;

public class ShowDetailDTO {
    private ShowDTO show;
    private List<SeatDTO> seats;
    private Integer totalSeats;
    private Integer availableSeats;
    private Integer bookedSeats;

    public ShowDetailDTO() {}

    public ShowDetailDTO(ShowDTO show, List<SeatDTO> seats, Integer totalSeats, Integer availableSeats, Integer bookedSeats) {
        this.show = show;
        this.seats = seats;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.bookedSeats = bookedSeats;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ShowDTO show;
        private List<SeatDTO> seats;
        private Integer totalSeats;
        private Integer availableSeats;
        private Integer bookedSeats;

        public Builder show(ShowDTO show) { this.show = show; return this; }
        public Builder seats(List<SeatDTO> seats) { this.seats = seats; return this; }
        public Builder totalSeats(Integer totalSeats) { this.totalSeats = totalSeats; return this; }
        public Builder availableSeats(Integer availableSeats) { this.availableSeats = availableSeats; return this; }
        public Builder bookedSeats(Integer bookedSeats) { this.bookedSeats = bookedSeats; return this; }

        public ShowDetailDTO build() {
            return new ShowDetailDTO(show, seats, totalSeats, availableSeats, bookedSeats);
        }
    }

    public ShowDTO getShow() { return show; }
    public void setShow(ShowDTO show) { this.show = show; }
    public List<SeatDTO> getSeats() { return seats; }
    public void setSeats(List<SeatDTO> seats) { this.seats = seats; }
    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
    public Integer getBookedSeats() { return bookedSeats; }
    public void setBookedSeats(Integer bookedSeats) { this.bookedSeats = bookedSeats; }
}
