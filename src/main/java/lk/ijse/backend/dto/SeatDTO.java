package lk.ijse.backend.dto;

import lk.ijse.backend.util.SeatType;

public class SeatDTO {
    private Long id;
    private Long screenId;
    private String seatRow;
    private Integer seatNumber;
    private SeatType seatType;
    private Double price;
    private Boolean isBooked = false;

    public SeatDTO() {}

    public SeatDTO(Long id, Long screenId, String seatRow, Integer seatNumber, SeatType seatType, Double price, Boolean isBooked) {
        this.id = id;
        this.screenId = screenId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
        this.isBooked = isBooked != null ? isBooked : false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long screenId;
        private String seatRow;
        private Integer seatNumber;
        private SeatType seatType;
        private Double price;
        private Boolean isBooked = false;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder screenId(Long screenId) { this.screenId = screenId; return this; }
        public Builder seatRow(String seatRow) { this.seatRow = seatRow; return this; }
        public Builder seatNumber(Integer seatNumber) { this.seatNumber = seatNumber; return this; }
        public Builder seatType(SeatType seatType) { this.seatType = seatType; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder isBooked(Boolean isBooked) { this.isBooked = isBooked; return this; }

        public SeatDTO build() {
            return new SeatDTO(id, screenId, seatRow, seatNumber, seatType, price, isBooked);
        }
    }

    public String getSeatIdentifier() {
        return seatRow + seatNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getScreenId() { return screenId; }
    public void setScreenId(Long screenId) { this.screenId = screenId; }
    public String getSeatRow() { return seatRow; }
    public void setSeatRow(String seatRow) { this.seatRow = seatRow; }
    public Integer getSeatNumber() { return seatNumber; }
    public void setSeatNumber(Integer seatNumber) { this.seatNumber = seatNumber; }
    public SeatType getSeatType() { return seatType; }
    public void setSeatType(SeatType seatType) { this.seatType = seatType; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Boolean getIsBooked() { return isBooked; }
    public void setIsBooked(Boolean isBooked) { this.isBooked = isBooked; }
}
