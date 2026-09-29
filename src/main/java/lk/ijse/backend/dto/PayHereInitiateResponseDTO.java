package lk.ijse.backend.dto;

public class PayHereInitiateResponseDTO {

    private Boolean sandbox = true;
    private String merchantId;
    private String orderId;
    private String bookingReference;
    private String items;
    private Double amount;
    private String formattedAmount;
    private String currency;
    private String hash;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address = "Scope Cinemas Multiplex";
    private String city = "Colombo";
    private String country = "Sri Lanka";
    private String notifyUrl;
    private String returnUrl;
    private String cancelUrl;

    public PayHereInitiateResponseDTO() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean sandbox = true;
        private String merchantId;
        private String orderId;
        private String bookingReference;
        private String items;
        private Double amount;
        private String formattedAmount;
        private String currency;
        private String hash;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String notifyUrl;
        private String returnUrl;
        private String cancelUrl;

        public Builder sandbox(Boolean sandbox) { this.sandbox = sandbox; return this; }
        public Builder merchantId(String merchantId) { this.merchantId = merchantId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder items(String items) { this.items = items; return this; }
        public Builder amount(Double amount) { this.amount = amount; return this; }
        public Builder formattedAmount(String formattedAmount) { this.formattedAmount = formattedAmount; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder hash(String hash) { this.hash = hash; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder notifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; return this; }
        public Builder returnUrl(String returnUrl) { this.returnUrl = returnUrl; return this; }
        public Builder cancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; return this; }

        public PayHereInitiateResponseDTO build() {
            PayHereInitiateResponseDTO dto = new PayHereInitiateResponseDTO();
            dto.setSandbox(this.sandbox);
            dto.setMerchantId(this.merchantId);
            dto.setOrderId(this.orderId);
            dto.setBookingReference(this.bookingReference);
            dto.setItems(this.items);
            dto.setAmount(this.amount);
            dto.setFormattedAmount(this.formattedAmount);
            dto.setCurrency(this.currency);
            dto.setHash(this.hash);
            dto.setFirstName(this.firstName);
            dto.setLastName(this.lastName);
            dto.setEmail(this.email);
            dto.setPhone(this.phone);
            dto.setNotifyUrl(this.notifyUrl);
            dto.setReturnUrl(this.returnUrl);
            dto.setCancelUrl(this.cancelUrl);
            return dto;
        }
    }

    public Boolean getSandbox() { return sandbox; }
    public void setSandbox(Boolean sandbox) { this.sandbox = sandbox; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getFormattedAmount() { return formattedAmount; }
    public void setFormattedAmount(String formattedAmount) { this.formattedAmount = formattedAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getNotifyUrl() { return notifyUrl; }
    public void setNotifyUrl(String notifyUrl) { this.notifyUrl = notifyUrl; }

    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }

    public String getCancelUrl() { return cancelUrl; }
    public void setCancelUrl(String cancelUrl) { this.cancelUrl = cancelUrl; }
}
