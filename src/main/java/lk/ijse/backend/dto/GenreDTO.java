package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class GenreDTO {
    private Long id;

    @NotBlank(message = "Genre name is required")
    private String name;

    public GenreDTO() {}

    public GenreDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }

        public GenreDTO build() {
            return new GenreDTO(id, name);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
