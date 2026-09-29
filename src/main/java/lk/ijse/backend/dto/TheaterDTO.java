package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TheaterDTO {
    private Long id;

    @NotBlank(message = "Theater name is required")
    private String name;

    @NotBlank(message = "Theater location is required")
    private String location;

    private Integer totalScreens;

    public TheaterDTO() {}

    public TheaterDTO(Long id, String name, String location, Integer totalScreens) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalScreens = totalScreens;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String location;
        private Integer totalScreens;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder totalScreens(Integer totalScreens) { this.totalScreens = totalScreens; return this; }

        public TheaterDTO build() {
            return new TheaterDTO(id, name, location, totalScreens);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getTotalScreens() { return totalScreens; }
    public void setTotalScreens(Integer totalScreens) { this.totalScreens = totalScreens; }
}
