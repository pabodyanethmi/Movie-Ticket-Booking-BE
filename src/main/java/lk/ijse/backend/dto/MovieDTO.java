package lk.ijse.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lk.ijse.backend.entity.MovieStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class MovieDTO {
    private Long id;

    @NotBlank(message = "Movie title is required")
    private String title;

    private String description;
    private String synopsis;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMins;

    @NotBlank(message = "Language is required")
    private String language;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    private String posterUrl;
    private String bannerUrl;
    private String backdropUrl;
    private String trailerUrl;
    private String galleryUrls;
    private String genre;
    private String formatTags;
    private MovieStatus status;
    private Double rating;

    private Boolean isFeatured = false;
    private Integer featuredOrder = 0;

    private String directors;
    private String cast;
    private String classification;

    private Set<Long> genreIds;
    private List<String> genreNames;

    public MovieDTO() {}

    public MovieDTO(Long id, String title, String description, String synopsis, Integer durationMins, String language, LocalDate releaseDate, String posterUrl, String bannerUrl, String backdropUrl, String trailerUrl, String galleryUrls, String genre, String formatTags, MovieStatus status, Double rating, Boolean isFeatured, Integer featuredOrder, String directors, String cast, String classification, Set<Long> genreIds, List<String> genreNames) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.synopsis = synopsis != null ? synopsis : description;
        this.durationMins = durationMins;
        this.language = language;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
        this.bannerUrl = bannerUrl;
        this.backdropUrl = backdropUrl;
        this.trailerUrl = trailerUrl;
        this.galleryUrls = galleryUrls;
        this.genre = genre;
        this.formatTags = formatTags;
        this.status = status;
        this.rating = rating;
        this.isFeatured = isFeatured != null ? isFeatured : false;
        this.featuredOrder = featuredOrder != null ? featuredOrder : 0;
        this.directors = directors;
        this.cast = cast;
        this.classification = classification;
        this.genreIds = genreIds;
        this.genreNames = genreNames;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private String synopsis;
        private Integer durationMins;
        private String language;
        private LocalDate releaseDate;
        private String posterUrl;
        private String bannerUrl;
        private String backdropUrl;
        private String trailerUrl;
        private String galleryUrls;
        private String genre;
        private String formatTags;
        private MovieStatus status;
        private Double rating;
        private Boolean isFeatured = false;
        private Integer featuredOrder = 0;
        private String directors;
        private String cast;
        private String classification;
        private Set<Long> genreIds;
        private List<String> genreNames;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder synopsis(String synopsis) { this.synopsis = synopsis; return this; }
        public Builder durationMins(Integer durationMins) { this.durationMins = durationMins; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder releaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; return this; }
        public Builder posterUrl(String posterUrl) { this.posterUrl = posterUrl; return this; }
        public Builder bannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; return this; }
        public Builder backdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; return this; }
        public Builder trailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; return this; }
        public Builder galleryUrls(String galleryUrls) { this.galleryUrls = galleryUrls; return this; }
        public Builder genre(String genre) { this.genre = genre; return this; }
        public Builder formatTags(String formatTags) { this.formatTags = formatTags; return this; }
        public Builder status(MovieStatus status) { this.status = status; return this; }
        public Builder rating(Double rating) { this.rating = rating; return this; }
        public Builder isFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; return this; }
        public Builder featuredOrder(Integer featuredOrder) { this.featuredOrder = featuredOrder; return this; }
        public Builder directors(String directors) { this.directors = directors; return this; }
        public Builder cast(String cast) { this.cast = cast; return this; }
        public Builder classification(String classification) { this.classification = classification; return this; }
        public Builder genreIds(Set<Long> genreIds) { this.genreIds = genreIds; return this; }
        public Builder genreNames(List<String> genreNames) { this.genreNames = genreNames; return this; }

        public MovieDTO build() {
            return new MovieDTO(id, title, description, synopsis, durationMins, language, releaseDate, posterUrl, bannerUrl, backdropUrl, trailerUrl, galleryUrls, genre, formatTags, status, rating, isFeatured, featuredOrder, directors, cast, classification, genreIds, genreNames);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; if (this.synopsis == null) this.synopsis = description; }
    public String getSynopsis() { return synopsis != null ? synopsis : description; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; if (this.description == null) this.description = synopsis; }
    public Integer getDurationMins() { return durationMins; }
    public void setDurationMins(Integer durationMins) { this.durationMins = durationMins; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getBannerUrl() { return bannerUrl; }
    public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
    public String getBackdropUrl() { return backdropUrl; }
    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; }
    public String getTrailerUrl() { return trailerUrl; }
    public void setTrailerUrl(String trailerUrl) { this.trailerUrl = trailerUrl; }
    public String getGalleryUrls() { return galleryUrls; }
    public void setGalleryUrls(String galleryUrls) { this.galleryUrls = galleryUrls; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getFormatTags() { return formatTags; }
    public void setFormatTags(String formatTags) { this.formatTags = formatTags; }
    public MovieStatus getStatus() { return status; }
    public void setStatus(MovieStatus status) { this.status = status; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured != null ? isFeatured : false; }
    public Integer getFeaturedOrder() { return featuredOrder; }
    public void setFeaturedOrder(Integer featuredOrder) { this.featuredOrder = featuredOrder != null ? featuredOrder : 0; }
    public String getDirectors() { return directors; }
    public void setDirectors(String directors) { this.directors = directors; }
    public String getCast() { return cast; }
    public void setCast(String cast) { this.cast = cast; }
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }
    public Set<Long> getGenreIds() { return genreIds; }
    public void setGenreIds(Set<Long> genreIds) { this.genreIds = genreIds; }
    public List<String> getGenreNames() { return genreNames; }
    public void setGenreNames(List<String> genreNames) { this.genreNames = genreNames; }
}
