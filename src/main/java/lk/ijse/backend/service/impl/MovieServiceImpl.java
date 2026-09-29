package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.MovieDTO;
import lk.ijse.backend.entity.Genre;
import lk.ijse.backend.entity.Movie;
import lk.ijse.backend.entity.MovieStatus;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.GenreRepository;
import lk.ijse.backend.repository.MovieRepository;
import lk.ijse.backend.repository.ReviewRepository;
import lk.ijse.backend.service.MovieService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;

    public MovieServiceImpl(MovieRepository movieRepository,
                             GenreRepository genreRepository,
                             ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Set<Genre> genres = new HashSet<>();
        if (movieDTO.getGenreIds() != null && !movieDTO.getGenreIds().isEmpty()) {
            genres = new HashSet<>(genreRepository.findAllById(movieDTO.getGenreIds()));
        }

        Movie movie = Movie.builder()
                .title(movieDTO.getTitle().trim())
                .description(movieDTO.getDescription() != null ? movieDTO.getDescription() : movieDTO.getSynopsis())
                .synopsis(movieDTO.getSynopsis() != null ? movieDTO.getSynopsis() : movieDTO.getDescription())
                .durationMins(movieDTO.getDurationMins())
                .language(movieDTO.getLanguage().trim())
                .releaseDate(movieDTO.getReleaseDate())
                .posterUrl(movieDTO.getPosterUrl())
                .bannerUrl(movieDTO.getBannerUrl())
                .backdropUrl(movieDTO.getBackdropUrl())
                .trailerUrl(movieDTO.getTrailerUrl())
                .galleryUrls(movieDTO.getGalleryUrls())
                .genre(movieDTO.getGenre())
                .formatTags(movieDTO.getFormatTags())
                .status(movieDTO.getStatus() != null ? movieDTO.getStatus() : MovieStatus.NOW_SHOWING)
                .rating(movieDTO.getRating() != null ? movieDTO.getRating() : 0.0)
                .isFeatured(movieDTO.getIsFeatured() != null ? movieDTO.getIsFeatured() : false)
                .featuredOrder(movieDTO.getFeaturedOrder() != null ? movieDTO.getFeaturedOrder() : 0)
                .directors(movieDTO.getDirectors())
                .cast(movieDTO.getCast())
                .classification(movieDTO.getClassification() != null ? movieDTO.getClassification() : "U")
                .genres(genres)
                .build();

        Movie saved = movieRepository.save(movie);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return mapToDTO(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAllByOrderByReleaseDateDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> searchMovies(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMovies();
        }
        return movieRepository.findByTitleContainingIgnoreCase(query.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreName(genre.trim()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getNowShowingMovies() {
        List<Movie> nowShowing = movieRepository.findByStatus(MovieStatus.NOW_SHOWING);
        if (nowShowing.isEmpty()) {
            nowShowing = movieRepository.findNowShowingMovies();
        }
        if (nowShowing.isEmpty()) {
            return movieRepository.findAllByOrderByReleaseDateDesc().stream().limit(8).map(this::mapToDTO).collect(Collectors.toList());
        }
        return nowShowing.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getFeaturedMovies() {
        List<Movie> featured = movieRepository.findByIsFeaturedTrueOrderByFeaturedOrderAsc();
        if (featured.isEmpty()) {
            featured = movieRepository.findTop5ByBannerUrlIsNotNullOrderByRatingDesc();
        }
        if (featured.isEmpty()) {
            featured = movieRepository.findAllByOrderByReleaseDateDesc().stream().limit(5).collect(Collectors.toList());
        }
        return featured.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getMoviesByStatus(MovieStatus status) {
        return movieRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieDTO> getRelatedMovies(Long id) {
        List<Movie> related = movieRepository.findTop6ByIdNotOrderByRatingDesc(id);
        if (related.isEmpty()) {
            related = movieRepository.findAllByOrderByReleaseDateDesc().stream()
                    .filter(m -> !m.getId().equals(id))
                    .limit(6)
                    .collect(Collectors.toList());
        }
        return related.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        movie.setTitle(movieDTO.getTitle().trim());
        if (movieDTO.getDescription() != null) movie.setDescription(movieDTO.getDescription());
        if (movieDTO.getSynopsis() != null) movie.setSynopsis(movieDTO.getSynopsis());
        movie.setDurationMins(movieDTO.getDurationMins());
        movie.setLanguage(movieDTO.getLanguage().trim());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        if (movieDTO.getPosterUrl() != null) movie.setPosterUrl(movieDTO.getPosterUrl());
        if (movieDTO.getBannerUrl() != null) movie.setBannerUrl(movieDTO.getBannerUrl());
        if (movieDTO.getBackdropUrl() != null) movie.setBackdropUrl(movieDTO.getBackdropUrl());
        if (movieDTO.getTrailerUrl() != null) movie.setTrailerUrl(movieDTO.getTrailerUrl());
        if (movieDTO.getGalleryUrls() != null) movie.setGalleryUrls(movieDTO.getGalleryUrls());
        if (movieDTO.getGenre() != null) movie.setGenre(movieDTO.getGenre());
        if (movieDTO.getFormatTags() != null) movie.setFormatTags(movieDTO.getFormatTags());
        if (movieDTO.getStatus() != null) movie.setStatus(movieDTO.getStatus());
        if (movieDTO.getRating() != null) movie.setRating(movieDTO.getRating());
        if (movieDTO.getIsFeatured() != null) movie.setIsFeatured(movieDTO.getIsFeatured());
        if (movieDTO.getFeaturedOrder() != null) movie.setFeaturedOrder(movieDTO.getFeaturedOrder());
        if (movieDTO.getDirectors() != null) movie.setDirectors(movieDTO.getDirectors());
        if (movieDTO.getCast() != null) movie.setCast(movieDTO.getCast());
        if (movieDTO.getClassification() != null) movie.setClassification(movieDTO.getClassification());

        if (movieDTO.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(movieDTO.getGenreIds()));
            movie.setGenres(genres);
        }

        Movie updated = movieRepository.save(movie);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public MovieDTO toggleFeatured(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        boolean nextFeaturedState = !Boolean.TRUE.equals(movie.getIsFeatured());
        movie.setIsFeatured(nextFeaturedState);

        Movie updated = movieRepository.save(movie);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private MovieDTO mapToDTO(Movie movie) {
        Set<Long> genreIds = movie.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        List<String> genreNames = movie.getGenres().stream().map(Genre::getName).collect(Collectors.toList());

        Double avgRating = reviewRepository.getAverageRatingByMovieId(movie.getId());
        double calculatedRating = (avgRating != null && avgRating > 0) ? Math.round(avgRating * 10.0) / 10.0 : (movie.getRating() != null ? movie.getRating() : 0.0);

        return MovieDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .synopsis(movie.getSynopsis())
                .durationMins(movie.getDurationMins())
                .language(movie.getLanguage())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .bannerUrl(movie.getBannerUrl())
                .backdropUrl(movie.getBackdropUrl())
                .trailerUrl(movie.getTrailerUrl())
                .galleryUrls(movie.getGalleryUrls())
                .genre(movie.getGenre())
                .formatTags(movie.getFormatTags())
                .status(movie.getStatus())
                .rating(calculatedRating)
                .isFeatured(movie.getIsFeatured())
                .featuredOrder(movie.getFeaturedOrder())
                .directors(movie.getDirectors())
                .cast(movie.getCast())
                .classification(movie.getClassification())
                .genreIds(genreIds)
                .genreNames(genreNames)
                .build();
    }
}
