package lk.ijse.backend.controller;

import lk.ijse.backend.dto.AdminDashboardDTO;
import lk.ijse.backend.dto.BookingResponseDTO;
import lk.ijse.backend.dto.MovieDTO;
import lk.ijse.backend.repository.*;
import lk.ijse.backend.service.BookingService;
import lk.ijse.backend.service.MovieService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final MovieService movieService;

    public AdminController(UserRepository userRepository,
                           MovieRepository movieRepository,
                           TheaterRepository theaterRepository,
                           ScreenRepository screenRepository,
                           ShowRepository showRepository,
                           BookingRepository bookingRepository,
                           BookingService bookingService,
                           MovieService movieService) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
        this.movieService = movieService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StandardResponse<AdminDashboardDTO>> getDashboardStats() {
        long totalUsers = userRepository.count();
        long totalMovies = movieRepository.count();
        long totalTheaters = theaterRepository.count();
        long totalScreens = screenRepository.count();
        long totalShows = showRepository.count();
        long totalBookings = bookingRepository.count();
        Double totalRevenue = bookingRepository.calculateTotalRevenue();

        List<BookingResponseDTO> recentBookings = bookingRepository.findTop10ByOrderByBookingTimeDesc().stream()
                .map(b -> bookingService.getBookingById(b.getId()))
                .collect(Collectors.toList());

        List<MovieDTO> popularMovies = movieService.getAllMovies().stream()
                .limit(5)
                .collect(Collectors.toList());

        AdminDashboardDTO stats = AdminDashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalMovies(totalMovies)
                .totalTheaters(totalTheaters)
                .totalScreens(totalScreens)
                .totalShows(totalShows)
                .totalBookings(totalBookings)
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .recentBookings(recentBookings)
                .popularMovies(popularMovies)
                .build();

        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Admin dashboard KPIs fetched successfully", stats)
        );
    }
}
