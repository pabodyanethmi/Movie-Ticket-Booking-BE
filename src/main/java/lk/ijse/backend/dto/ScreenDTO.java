package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotNull;

public class ScreenDTO {
    private Long id;

    @NotNull(message = "Theater ID is required")
    private Long theaterId;

    private String theaterName;

    @NotNull(message = "Screen number is required")
    private Integer screenNumber;

    private Integer seatCapacity;

    public ScreenDTO() {}

    public ScreenDTO(Long id, Long theaterId, String theaterName, Integer screenNumber, Integer seatCapacity) {
        this.id = id;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.screenNumber = screenNumber;
        this.seatCapacity = seatCapacity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long theaterId;
        private String theaterName;
        private Integer screenNumber;
        private Integer seatCapacity;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder theaterId(Long theaterId) { this.theaterId = theaterId; return this; }
        public Builder theaterName(String theaterName) { this.theaterName = theaterName; return this; }
        public Builder screenNumber(Integer screenNumber) { this.screenNumber = screenNumber; return this; }
        public Builder seatCapacity(Integer seatCapacity) { this.seatCapacity = seatCapacity; return this; }

        public ScreenDTO build() {
            return new ScreenDTO(id, theaterId, theaterName, screenNumber, seatCapacity);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTheaterId() { return theaterId; }
    public void setTheaterId(Long theaterId) { this.theaterId = theaterId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public Integer getScreenNumber() { return screenNumber; }
    public void setScreenNumber(Integer screenNumber) { this.screenNumber = screenNumber; }
    public Integer getSeatCapacity() { return seatCapacity; }
    public void setSeatCapacity(Integer seatCapacity) { this.seatCapacity = seatCapacity; }
}
