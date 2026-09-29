package lk.ijse.backend.dto;

public class ExperienceDTO {
    private Long id;
    private String title;
    private String description;
    private String iconUrl;
    private String bannerImageUrl;

    public ExperienceDTO() {}

    public ExperienceDTO(Long id, String title, String description, String iconUrl, String bannerImageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconUrl = iconUrl;
        this.bannerImageUrl = bannerImageUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }
}
