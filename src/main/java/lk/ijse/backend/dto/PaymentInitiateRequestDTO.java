package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentInitiateRequestDTO {

    private String orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String currency = "LKR";
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String items = "Movie Tickets & Concessions";

    public PaymentInitiateRequestDTO() {}

    public PaymentInitiateRequestDTO(String orderId, Double amount, String currency, String customerName, String customerEmail, String customerPhone, String items) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency != null ? currency : "LKR";
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.items = items != null ? items : "Movie Tickets & Concessions";
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
}
