package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lk.ijse.backend.util.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class BookingRequestDTO {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> seatIds;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Double totalAmount;
    private Double discountAmount;
    private String promoCode;
    private List<ConcessionItemDTO> concessions = new ArrayList<>();

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long showId, List<Long> seatIds, PaymentMethod paymentMethod) {
        this.showId = showId;
        this.seatIds = seatIds;
        this.paymentMethod = paymentMethod;
    }

    public static class ConcessionItemDTO {
        private Long id;
        private String name;
        private Double price;
        private Integer qty;

        public ConcessionItemDTO() {}

        public ConcessionItemDTO(Long id, String name, Double price, Integer qty) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.qty = qty;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Integer getQty() { return qty; }
        public void setQty(Integer qty) { this.qty = qty; }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long showId;
        private List<Long> seatIds;
        private PaymentMethod paymentMethod;
        private String customerName;
        private String customerEmail;
        private String customerPhone;
        private Double totalAmount;
        private Double discountAmount;
        private String promoCode;
        private List<ConcessionItemDTO> concessions = new ArrayList<>();

        public Builder showId(Long showId) { this.showId = showId; return this; }
        public Builder seatIds(List<Long> seatIds) { this.seatIds = seatIds; return this; }
        public Builder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder customerEmail(String customerEmail) { this.customerEmail = customerEmail; return this; }
        public Builder customerPhone(String customerPhone) { this.customerPhone = customerPhone; return this; }
        public Builder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder discountAmount(Double discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder promoCode(String promoCode) { this.promoCode = promoCode; return this; }
        public Builder concessions(List<ConcessionItemDTO> concessions) { this.concessions = concessions; return this; }

        public BookingRequestDTO build() {
            BookingRequestDTO dto = new BookingRequestDTO(showId, seatIds, paymentMethod);
            dto.setCustomerName(customerName);
            dto.setCustomerEmail(customerEmail);
            dto.setCustomerPhone(customerPhone);
            dto.setTotalAmount(totalAmount);
            dto.setDiscountAmount(discountAmount);
            dto.setPromoCode(promoCode);
            dto.setConcessions(concessions);
            return dto;
        }
    }

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Double discountAmount) { this.discountAmount = discountAmount; }

    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }

    public List<ConcessionItemDTO> getConcessions() { return concessions; }
    public void setConcessions(List<ConcessionItemDTO> concessions) { this.concessions = concessions; }
}
