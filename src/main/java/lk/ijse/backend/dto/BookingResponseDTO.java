package lk.ijse.backend.dto;

import lk.ijse.backend.util.BookingStatus;
import lk.ijse.backend.util.PaymentMethod;
import lk.ijse.backend.util.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponseDTO {
    private Long id;
    private String bookingReference;
    private Long userId;
    private String userName;
    private String userEmail;
    private String customerPhone;
    private Long showId;
    private String movieTitle;
    private String moviePosterUrl;
    private String theaterName;
    private String theaterLocation;
    private Integer screenNumber;
    private LocalDateTime showStartTime;
    private LocalDateTime showEndTime;
    private List<String> seatNumbers;
    private Double totalAmount;
    private BookingStatus bookingStatus;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private LocalDateTime bookingTime;
    private String qrCodePayload;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long id, String bookingReference, Long userId, String userName, String userEmail, String customerPhone, Long showId, String movieTitle, String moviePosterUrl, String theaterName, String theaterLocation, Integer screenNumber, LocalDateTime showStartTime, LocalDateTime showEndTime, List<String> seatNumbers, Double totalAmount, BookingStatus bookingStatus, PaymentStatus paymentStatus, PaymentMethod paymentMethod, String transactionReference, LocalDateTime bookingTime, String qrCodePayload) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.customerPhone = customerPhone;
        this.showId = showId;
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.screenNumber = screenNumber;
        this.showStartTime = showStartTime;
        this.showEndTime = showEndTime;
        this.seatNumbers = seatNumbers;
        this.totalAmount = totalAmount;
        this.bookingStatus = bookingStatus;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionReference = transactionReference;
        this.bookingTime = bookingTime;
        this.qrCodePayload = qrCodePayload;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String bookingReference;
        private Long userId;
        private String userName;
        private String userEmail;
        private String customerPhone;
        private Long showId;
        private String movieTitle;
        private String moviePosterUrl;
        private String theaterName;
        private String theaterLocation;
        private Integer screenNumber;
        private LocalDateTime showStartTime;
        private LocalDateTime showEndTime;
        private List<String> seatNumbers;
        private Double totalAmount;
        private BookingStatus bookingStatus;
        private PaymentStatus paymentStatus;
        private PaymentMethod paymentMethod;
        private String transactionReference;
        private LocalDateTime bookingTime;
        private String qrCodePayload;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder userName(String userName) { this.userName = userName; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder customerPhone(String customerPhone) { this.customerPhone = customerPhone; return this; }
        public Builder showId(Long showId) { this.showId = showId; return this; }
        public Builder movieTitle(String movieTitle) { this.movieTitle = movieTitle; return this; }
        public Builder moviePosterUrl(String moviePosterUrl) { this.moviePosterUrl = moviePosterUrl; return this; }
        public Builder theaterName(String theaterName) { this.theaterName = theaterName; return this; }
        public Builder theaterLocation(String theaterLocation) { this.theaterLocation = theaterLocation; return this; }
        public Builder screenNumber(Integer screenNumber) { this.screenNumber = screenNumber; return this; }
        public Builder showStartTime(LocalDateTime showStartTime) { this.showStartTime = showStartTime; return this; }
        public Builder showEndTime(LocalDateTime showEndTime) { this.showEndTime = showEndTime; return this; }
        public Builder seatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder bookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }
        public Builder bookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; return this; }
        public Builder qrCodePayload(String qrCodePayload) { this.qrCodePayload = qrCodePayload; return this; }

        public BookingResponseDTO build() {
            return new BookingResponseDTO(id, bookingReference, userId, userName, userEmail, customerPhone, showId, movieTitle, moviePosterUrl, theaterName, theaterLocation, screenNumber, showStartTime, showEndTime, seatNumbers, totalAmount, bookingStatus, paymentStatus, paymentMethod, transactionReference, bookingTime, qrCodePayload);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getMoviePosterUrl() { return moviePosterUrl; }
    public void setMoviePosterUrl(String moviePosterUrl) { this.moviePosterUrl = moviePosterUrl; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getTheaterLocation() { return theaterLocation; }
    public void setTheaterLocation(String theaterLocation) { this.theaterLocation = theaterLocation; }
    public Integer getScreenNumber() { return screenNumber; }
    public void setScreenNumber(Integer screenNumber) { this.screenNumber = screenNumber; }
    public LocalDateTime getShowStartTime() { return showStartTime; }
    public void setShowStartTime(LocalDateTime showStartTime) { this.showStartTime = showStartTime; }
    public LocalDateTime getShowEndTime() { return showEndTime; }
    public void setShowEndTime(LocalDateTime showEndTime) { this.showEndTime = showEndTime; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public String getQrCodePayload() { return qrCodePayload; }
    public void setQrCodePayload(String qrCodePayload) { this.qrCodePayload = qrCodePayload; }
}
