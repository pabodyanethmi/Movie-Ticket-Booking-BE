package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ShowDTO {
    private Long id;

    @NotNull(message = "Movie ID is required")
    private Long movieId;
    private String movieTitle;
    private String moviePosterUrl;
    private String movieLanguage;
    private Integer movieDurationMins;
    private String movieGenre;
    private String movieFormatTags;

    @NotNull(message = "Screen ID is required")
    private Long screenId;
    private Integer screenNumber;

    private Long theaterId;
    private String theaterName;
    private String theaterLocation;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Ticket price is required")
    private Double ticketPrice;

    private String experience;

    public ShowDTO() {}

    public ShowDTO(Long id, Long movieId, String movieTitle, String moviePosterUrl, String movieLanguage, Integer movieDurationMins, String movieGenre, String movieFormatTags, Long screenId, Integer screenNumber, Long theaterId, String theaterName, String theaterLocation, LocalDateTime startTime, LocalDateTime endTime, Double ticketPrice, String experience) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.movieLanguage = movieLanguage;
        this.movieDurationMins = movieDurationMins;
        this.movieGenre = movieGenre;
        this.movieFormatTags = movieFormatTags;
        this.screenId = screenId;
        this.screenNumber = screenNumber;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterLocation = theaterLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
        this.experience = experience;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long movieId;
        private String movieTitle;
        private String moviePosterUrl;
        private String movieLanguage;
        private Integer movieDurationMins;
        private String movieGenre;
        private String movieFormatTags;
        private Long screenId;
        private Integer screenNumber;
        private Long theaterId;
        private String theaterName;
        private String theaterLocation;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double ticketPrice;
        private String experience;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder movieId(Long movieId) { this.movieId = movieId; return this; }
        public Builder movieTitle(String movieTitle) { this.movieTitle = movieTitle; return this; }
        public Builder moviePosterUrl(String moviePosterUrl) { this.moviePosterUrl = moviePosterUrl; return this; }
        public Builder movieLanguage(String movieLanguage) { this.movieLanguage = movieLanguage; return this; }
        public Builder movieDurationMins(Integer movieDurationMins) { this.movieDurationMins = movieDurationMins; return this; }
        public Builder movieGenre(String movieGenre) { this.movieGenre = movieGenre; return this; }
        public Builder movieFormatTags(String movieFormatTags) { this.movieFormatTags = movieFormatTags; return this; }
        public Builder screenId(Long screenId) { this.screenId = screenId; return this; }
        public Builder screenNumber(Integer screenNumber) { this.screenNumber = screenNumber; return this; }
        public Builder theaterId(Long theaterId) { this.theaterId = theaterId; return this; }
        public Builder theaterName(String theaterName) { this.theaterName = theaterName; return this; }
        public Builder theaterLocation(String theaterLocation) { this.theaterLocation = theaterLocation; return this; }
        public Builder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public Builder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public Builder ticketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; return this; }
        public Builder experience(String experience) { this.experience = experience; return this; }

        public ShowDTO build() {
            return new ShowDTO(id, movieId, movieTitle, moviePosterUrl, movieLanguage, movieDurationMins, movieGenre, movieFormatTags, screenId, screenNumber, theaterId, theaterName, theaterLocation, startTime, endTime, ticketPrice, experience);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getMoviePosterUrl() { return moviePosterUrl; }
    public void setMoviePosterUrl(String moviePosterUrl) { this.moviePosterUrl = moviePosterUrl; }
    public String getMovieLanguage() { return movieLanguage; }
    public void setMovieLanguage(String movieLanguage) { this.movieLanguage = movieLanguage; }
    public Integer getMovieDurationMins() { return movieDurationMins; }
    public void setMovieDurationMins(Integer movieDurationMins) { this.movieDurationMins = movieDurationMins; }
    public String getMovieGenre() { return movieGenre; }
    public void setMovieGenre(String movieGenre) { this.movieGenre = movieGenre; }
    public String getMovieFormatTags() { return movieFormatTags; }
    public void setMovieFormatTags(String movieFormatTags) { this.movieFormatTags = movieFormatTags; }
    public Long getScreenId() { return screenId; }
    public void setScreenId(Long screenId) { this.screenId = screenId; }
    public Integer getScreenNumber() { return screenNumber; }
    public void setScreenNumber(Integer screenNumber) { this.screenNumber = screenNumber; }
    public Long getTheaterId() { return theaterId; }
    public void setTheaterId(Long theaterId) { this.theaterId = theaterId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getTheaterLocation() { return theaterLocation; }
    public void setTheaterLocation(String theaterLocation) { this.theaterLocation = theaterLocation; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
}
