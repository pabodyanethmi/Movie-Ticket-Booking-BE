package lk.ijse.backend.dto;

import java.util.List;

public class AdminDashboardDTO {
    private Long totalUsers;
    private Long totalMovies;
    private Long totalTheaters;
    private Long totalScreens;
    private Long totalShows;
    private Long totalBookings;
    private Double totalRevenue;
    private List<BookingResponseDTO> recentBookings;
    private List<MovieDTO> popularMovies;

    public AdminDashboardDTO() {}

    public AdminDashboardDTO(Long totalUsers, Long totalMovies, Long totalTheaters, Long totalScreens, Long totalShows, Long totalBookings, Double totalRevenue, List<BookingResponseDTO> recentBookings, List<MovieDTO> popularMovies) {
        this.totalUsers = totalUsers;
        this.totalMovies = totalMovies;
        this.totalTheaters = totalTheaters;
        this.totalScreens = totalScreens;
        this.totalShows = totalShows;
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.recentBookings = recentBookings;
        this.popularMovies = popularMovies;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalUsers;
        private Long totalMovies;
        private Long totalTheaters;
        private Long totalScreens;
        private Long totalShows;
        private Long totalBookings;
        private Double totalRevenue;
        private List<BookingResponseDTO> recentBookings;
        private List<MovieDTO> popularMovies;

        public Builder totalUsers(Long totalUsers) { this.totalUsers = totalUsers; return this; }
        public Builder totalMovies(Long totalMovies) { this.totalMovies = totalMovies; return this; }
        public Builder totalTheaters(Long totalTheaters) { this.totalTheaters = totalTheaters; return this; }
        public Builder totalScreens(Long totalScreens) { this.totalScreens = totalScreens; return this; }
        public Builder totalShows(Long totalShows) { this.totalShows = totalShows; return this; }
        public Builder totalBookings(Long totalBookings) { this.totalBookings = totalBookings; return this; }
        public Builder totalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public Builder recentBookings(List<BookingResponseDTO> recentBookings) { this.recentBookings = recentBookings; return this; }
        public Builder popularMovies(List<MovieDTO> popularMovies) { this.popularMovies = popularMovies; return this; }

        public AdminDashboardDTO build() {
            return new AdminDashboardDTO(totalUsers, totalMovies, totalTheaters, totalScreens, totalShows, totalBookings, totalRevenue, recentBookings, popularMovies);
        }
    }

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }
    public Long getTotalMovies() { return totalMovies; }
    public void setTotalMovies(Long totalMovies) { this.totalMovies = totalMovies; }
    public Long getTotalTheaters() { return totalTheaters; }
    public void setTotalTheaters(Long totalTheaters) { this.totalTheaters = totalTheaters; }
    public Long getTotalScreens() { return totalScreens; }
    public void setTotalScreens(Long totalScreens) { this.totalScreens = totalScreens; }
    public Long getTotalShows() { return totalShows; }
    public void setTotalShows(Long totalShows) { this.totalShows = totalShows; }
    public Long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(Long totalBookings) { this.totalBookings = totalBookings; }
    public Double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
    public List<BookingResponseDTO> getRecentBookings() { return recentBookings; }
    public void setRecentBookings(List<BookingResponseDTO> recentBookings) { this.recentBookings = recentBookings; }
    public List<MovieDTO> getPopularMovies() { return popularMovies; }
    public void setPopularMovies(List<MovieDTO> popularMovies) { this.popularMovies = popularMovies; }
}
