package lk.ijse.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 200)
    private String subtitle;

    private Double discountPercent;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String bannerUrl;

    private LocalDate validUntil;

    @Column(length = 500)
    private String termsUrl;

    public Promotion() {}

    public Promotion(Long id, String title, String subtitle, Double discountPercent, String description, String bannerUrl, LocalDate validUntil, String termsUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.discountPercent = discountPercent;
        this.description = description;
        this.bannerUrl = bannerUrl;
        this.validUntil = validUntil;
        this.termsUrl = termsUrl;
    }

    public Promotion(String title, String subtitle, Double discountPercent, String description, String bannerUrl, LocalDate validUntil, String termsUrl) {
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
