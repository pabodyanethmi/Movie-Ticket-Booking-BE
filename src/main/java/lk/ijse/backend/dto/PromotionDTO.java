package lk.ijse.backend.dto;

import java.time.LocalDate;

public class PromotionDTO {
    private Long id;
    private String title;
    private String subtitle;
    private Double discountPercent;
    private String description;
    private String bannerUrl;
    private LocalDate validUntil;
    private String termsUrl;

    public PromotionDTO() {}

    public PromotionDTO(Long id, String title, String subtitle, Double discountPercent, String description, String bannerUrl, LocalDate validUntil, String termsUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.discountPercent = discountPercent;
        this.description = description;
        this.bannerUrl = bannerUrl;
        this.validUntil = validUntil;
        this.termsUrl = termsUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    public LocalDate getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public String getTermsUrl() { return termsUrl; }
    public void setTermsUrl(String termsUrl) { this.termsUrl = termsUrl; }
}
