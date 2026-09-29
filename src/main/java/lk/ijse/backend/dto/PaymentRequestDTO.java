package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lk.ijse.backend.util.PaymentMethod;

public class PaymentRequestDTO {

    @NotNull(message = "Booking ID is required")
    private Long bookingId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    public PaymentRequestDTO() {}

    public PaymentRequestDTO(Long bookingId, PaymentMethod paymentMethod, Double amount) {
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long bookingId;
        private PaymentMethod paymentMethod;
        private Double amount;

        public Builder bookingId(Long bookingId) { this.bookingId = bookingId; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder amount(Double amount) { this.amount = amount; return this; }

        public PaymentRequestDTO build() {
            return new PaymentRequestDTO(bookingId, paymentMethod, amount);
        }
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
