package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

public class PayHereInitiateRequestDTO {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> seatIds;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String currency = "LKR";
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String items = "Movie Tickets - Scope Cinemas";
    private List<BookingRequestDTO.ConcessionItemDTO> concessions = new ArrayList<>();

    public PayHereInitiateRequestDTO() {}

    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }

    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }

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

    public List<BookingRequestDTO.ConcessionItemDTO> getConcessions() { return concessions; }
    public void setConcessions(List<BookingRequestDTO.ConcessionItemDTO> concessions) { this.concessions = concessions; }
}
