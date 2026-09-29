package lk.ijse.backend.dto;

import lk.ijse.backend.util.PaymentMethod;
import lk.ijse.backend.util.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponseDTO {
    private Long id;
    private Long bookingId;
    private PaymentMethod paymentMethod;
    private Double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime transactionTime;
    private String transactionReference;

    public PaymentResponseDTO() {}

    public PaymentResponseDTO(Long id, Long bookingId, PaymentMethod paymentMethod, Double amount, PaymentStatus paymentStatus, LocalDateTime transactionTime, String transactionReference) {
        this.id = id;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.transactionTime = transactionTime;
        this.transactionReference = transactionReference;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long bookingId;
        private PaymentMethod paymentMethod;
        private Double amount;
        private PaymentStatus paymentStatus;
        private LocalDateTime transactionTime;
        private String transactionReference;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder amount(Double amount) { this.amount = amount; return this; }
        public Builder paymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public Builder transactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; return this; }
        public Builder transactionReference(String transactionReference) { this.transactionReference = transactionReference; return this; }

        public PaymentResponseDTO build() {
            return new PaymentResponseDTO(id, bookingId, paymentMethod, amount, paymentStatus, transactionTime, transactionReference);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
}
