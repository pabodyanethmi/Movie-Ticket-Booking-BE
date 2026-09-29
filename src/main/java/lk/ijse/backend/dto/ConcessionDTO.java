package lk.ijse.backend.dto;

public class ConcessionDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private String imageUrl;
    private String description;
    private Boolean isAvailable = true;

    public ConcessionDTO() {}

    public ConcessionDTO(Long id, String name, String category, Double price, String imageUrl, String description, Boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isAvailable = isAvailable != null ? isAvailable : true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String category;
        private Double price;
        private String imageUrl;
        private String description;
        private Boolean isAvailable = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder price(Double price) { this.price = price; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder isAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; return this; }

        public ConcessionDTO build() {
            return new ConcessionDTO(id, name, category, price, imageUrl, description, isAvailable);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable != null ? isAvailable : true; }
}
