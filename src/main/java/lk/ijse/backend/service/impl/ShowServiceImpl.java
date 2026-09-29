package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.SeatDTO;
import lk.ijse.backend.dto.ShowDTO;
import lk.ijse.backend.dto.ShowDetailDTO;
import lk.ijse.backend.entity.Movie;
import lk.ijse.backend.entity.Screen;
import lk.ijse.backend.entity.Show;
import lk.ijse.backend.exception.BadRequestException;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.MovieRepository;
import lk.ijse.backend.repository.ScreenRepository;
import lk.ijse.backend.repository.ShowRepository;
import lk.ijse.backend.service.SeatService;
import lk.ijse.backend.service.ShowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatService seatService;

    public ShowServiceImpl(ShowRepository showRepository,
                           MovieRepository movieRepository,
                           ScreenRepository screenRepository,
                           SeatService seatService) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.seatService = seatService;
    }

    @Override
    @Transactional
    public ShowDTO createShow(ShowDTO showDTO) {
        Movie movie = movieRepository.findById(showDTO.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + showDTO.getMovieId()));

        Screen screen = screenRepository.findById(showDTO.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + showDTO.getScreenId()));

        String expFormat = showDTO.getExperience() != null ? showDTO.getExperience() 
                : (showDTO.getMovieFormatTags() != null ? showDTO.getMovieFormatTags() : movie.getFormatTags());

        LocalDateTime startTime = showDTO.getStartTime();
        LocalDateTime endTime = showDTO.getEndTime();
        if (endTime == null && startTime != null) {
            int mins = movie.getDurationMins() != null ? movie.getDurationMins() : 120;
            endTime = startTime.plusMinutes(mins);
        }

        if (endTime != null && startTime != null && endTime.isBefore(startTime)) {
            throw new BadRequestException("Show end time cannot be before start time");
        }

        List<Show> conflicts = showRepository.findConflictingShows(
                screen.getId(),
                startTime,
                endTime
        );

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("Another show is already scheduled on Screen #" + screen.getScreenNumber() + " during this time slot.");
        }

        Show show = Show.builder()
                .movie(movie)
                .screen(screen)
                .startTime(startTime)
                .endTime(endTime)
                .ticketPrice(showDTO.getTicketPrice())
                .experience(expFormat)
                .build();

        Show saved = showRepository.save(show);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public List<ShowDTO> createBulkShows(lk.ijse.backend.dto.BulkShowRequestDTO bulkRequest) {
        Movie movie = movieRepository.findById(bulkRequest.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + bulkRequest.getMovieId()));

        int durationMins = movie.getDurationMins() != null ? movie.getDurationMins() : 120;
        List<ShowDTO> createdShows = new java.util.ArrayList<>();

        List<java.time.LocalDate> targetDates = new java.util.ArrayList<>();
        if ("SPECIFIC".equalsIgnoreCase(bulkRequest.getDateSelectionMode()) && bulkRequest.getSpecificDates() != null && !bulkRequest.getSpecificDates().isEmpty()) {
            targetDates.addAll(bulkRequest.getSpecificDates());
        } else if (bulkRequest.getStartDate() != null && bulkRequest.getEndDate() != null) {
            java.time.LocalDate current = bulkRequest.getStartDate();
            java.time.LocalDate end = bulkRequest.getEndDate();
            while (!current.isAfter(end)) {
                targetDates.add(current);
                current = current.plusDays(1);
            }
        } else if (bulkRequest.getStartDate() != null) {
            targetDates.add(bulkRequest.getStartDate());
        }

        List<Long> cinemaIds = bulkRequest.getCinemaIds();
        List<String> times = bulkRequest.getTimes();
        List<String> experiences = bulkRequest.getExperiences();
        if (experiences == null || experiences.isEmpty()) {
            experiences = java.util.Collections.singletonList(movie.getFormatTags() != null ? movie.getFormatTags() : "DOLBY ATMOS");
        }

        for (java.time.LocalDate date : targetDates) {
            for (Long cinemaId : cinemaIds) {
                List<Screen> screens = screenRepository.findByTheaterId(cinemaId);
                Screen screen = screens.isEmpty() ? screenRepository.findAll().stream().findFirst().orElse(null) : screens.get(0);
                if (screen == null) continue;

                for (String exp : experiences) {
                    for (String timeStr : times) {
                        try {
                            java.time.LocalTime time = parseLocalTime(timeStr);
                            java.time.LocalDateTime startTime = java.time.LocalDateTime.of(date, time);
                            java.time.LocalDateTime endTime = startTime.plusMinutes(durationMins);

                            boolean exists = showRepository.existsShowSlot(
                                    cinemaId, bulkRequest.getMovieId(), startTime
                            );
                            if (exists) {
                                continue;
                            }

                            List<Show> conflicts = showRepository.findConflictingShows(screen.getId(), startTime, endTime);
                            if (!conflicts.isEmpty()) {
                                continue;
                            }

                            Show show = Show.builder()
                                    .movie(movie)
                                    .screen(screen)
                                    .startTime(startTime)
                                    .endTime(endTime)
                                    .ticketPrice(bulkRequest.getTicketPrice())
                                    .experience(exp)
                                    .build();

                            Show saved = showRepository.save(show);
                            ShowDTO dto = mapToDTO(saved);
                            createdShows.add(dto);
                        } catch (Exception e) {
                            // Ignore single slot error
                        }
                    }
                }
            }
        }

        return createdShows;
    }

    private java.time.LocalTime parseLocalTime(String timeStr) {
        if (timeStr == null) return java.time.LocalTime.of(10, 0);
        String trimmed = timeStr.trim();
        if (trimmed.matches("^\\d{1,2}:\\d{2}$")) {
            String[] parts = trimmed.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            return java.time.LocalTime.of(h, m);
        }
        if (trimmed.toUpperCase().contains("AM") || trimmed.toUpperCase().contains("PM")) {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("hh:mm a", java.util.Locale.ENGLISH);
            return java.time.LocalTime.parse(trimmed.toUpperCase(), fmt);
        }
        return java.time.LocalTime.of(10, 0);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowDTO getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));
        return mapToDTO(show);
    }

    @Override
    @Transactional(readOnly = true)
    public ShowDetailDTO getShowDetailsById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        List<SeatDTO> seats = seatService.getSeatsByShowId(id);
        int totalSeats = seats.size();
        int bookedSeats = (int) seats.stream().filter(SeatDTO::getIsBooked).count();
        int availableSeats = totalSeats - bookedSeats;

        return ShowDetailDTO.builder()
                .show(mapToDTO(show))
                .seats(seats)
                .totalSeats(totalSeats)
                .bookedSeats(bookedSeats)
                .availableSeats(availableSeats)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDTO> getAllShows() {
        return showRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDTO> getShowsByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDTO> getUpcomingShowsByMovieId(Long movieId) {
        return showRepository.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(movieId, LocalDateTime.now().minusHours(1)).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShowDTO> getShowsByScreenId(Long screenId) {
        return showRepository.findByScreenId(screenId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShowDTO updateShow(Long id, ShowDTO showDTO) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        if (showDTO.getMovieId() != null) {
            Movie movie = movieRepository.findById(showDTO.getMovieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + showDTO.getMovieId()));
            show.setMovie(movie);
        }

        if (showDTO.getScreenId() != null) {
            Screen screen = screenRepository.findById(showDTO.getScreenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + showDTO.getScreenId()));
            show.setScreen(screen);
        }

        if (showDTO.getStartTime() != null) show.setStartTime(showDTO.getStartTime());
        if (showDTO.getEndTime() != null) show.setEndTime(showDTO.getEndTime());
        if (showDTO.getTicketPrice() != null) show.setTicketPrice(showDTO.getTicketPrice());
        if (showDTO.getExperience() != null) show.setExperience(showDTO.getExperience());

        Show updated = showRepository.save(show);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new ResourceNotFoundException("Show not found with id: " + id);
        }
        showRepository.deleteById(id);
    }

    private ShowDTO mapToDTO(Show show) {
        String exp = show.getExperience();
        if (exp == null || exp.trim().isEmpty()) {
            exp = show.getMovie() != null ? show.getMovie().getFormatTags() : "DOLBY ATMOS";
        }
        return ShowDTO.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .moviePosterUrl(show.getMovie().getPosterUrl())
                .movieLanguage(show.getMovie().getLanguage())
                .movieDurationMins(show.getMovie().getDurationMins())
                .movieGenre(show.getMovie().getGenre())
                .movieFormatTags(show.getMovie().getFormatTags())
                .screenId(show.getScreen().getId())
                .screenNumber(show.getScreen().getScreenNumber())
                .theaterId(show.getScreen().getTheater().getId())
                .theaterName(show.getScreen().getTheater().getName())
                .theaterLocation(show.getScreen().getTheater().getLocation())
                .startTime(show.getStartTime())
                .endTime(show.getEndTime())
                .ticketPrice(show.getTicketPrice())
                .experience(exp)
                .build();
    }
}
