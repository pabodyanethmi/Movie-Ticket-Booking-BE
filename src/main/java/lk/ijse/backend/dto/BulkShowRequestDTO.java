package lk.ijse.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class BulkShowRequestDTO {

    private String dateSelectionMode;

    private List<LocalDate> specificDates;

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotEmpty(message = "At least one cinema location is required")
    private List<Long> cinemaIds;

    private List<String> experiences;

    @NotEmpty(message = "At least one time slot is required")
    private List<String> times;

    @NotNull(message = "Ticket price is required")
    private Double ticketPrice;

    public BulkShowRequestDTO() {}

    public BulkShowRequestDTO(String dateSelectionMode, List<LocalDate> specificDates, Long movieId, LocalDate startDate, LocalDate endDate, List<Long> cinemaIds, List<String> experiences, List<String> times, Double ticketPrice) {
        this.dateSelectionMode = dateSelectionMode;
        this.specificDates = specificDates;
        this.movieId = movieId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cinemaIds = cinemaIds;
        this.experiences = experiences;
        this.times = times;
        this.ticketPrice = ticketPrice;
    }

    public String getDateSelectionMode() { return dateSelectionMode; }
    public void setDateSelectionMode(String dateSelectionMode) { this.dateSelectionMode = dateSelectionMode; }

    public List<LocalDate> getSpecificDates() { return specificDates; }
    public void setSpecificDates(List<LocalDate> specificDates) { this.specificDates = specificDates; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public List<Long> getCinemaIds() { return cinemaIds; }
    public void setCinemaIds(List<Long> cinemaIds) { this.cinemaIds = cinemaIds; }

    public List<String> getExperiences() { return experiences; }
    public void setExperiences(List<String> experiences) { this.experiences = experiences; }

    public List<String> getTimes() { return times; }
    public void setTimes(List<String> times) { this.times = times; }

    public Double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Double ticketPrice) { this.ticketPrice = ticketPrice; }
}
