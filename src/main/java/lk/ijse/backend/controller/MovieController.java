package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.MovieDTO;
import lk.ijse.backend.entity.MovieStatus;
import lk.ijse.backend.service.MovieService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<MovieDTO>>> getAllMovies(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) MovieStatus status) {
        List<MovieDTO> movies;
        if (status != null) {
            movies = movieService.getMoviesByStatus(status);
        } else if (search != null && !search.trim().isEmpty()) {
            movies = movieService.searchMovies(search);
        } else if (genre != null && !genre.trim().isEmpty()) {
            movies = movieService.getMoviesByGenre(genre);
        } else {
            movies = movieService.getAllMovies();
        }
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movies fetched successfully", movies)
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<StandardResponse<List<MovieDTO>>> getFeaturedMovies() {
        List<MovieDTO> movies = movieService.getFeaturedMovies();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Featured movies fetched successfully", movies)
        );
    }

    @GetMapping("/now-showing")
    public ResponseEntity<StandardResponse<List<MovieDTO>>> getNowShowingMovies() {
        List<MovieDTO> movies = movieService.getNowShowingMovies();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Now showing movies fetched successfully", movies)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<MovieDTO>> getMovieById(@PathVariable Long id) {
        MovieDTO movie = movieService.getMovieById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movie details fetched successfully", movie)
        );
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<StandardResponse<List<MovieDTO>>> getRelatedMoviesById(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.getRelatedMovies(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Related movies fetched successfully", movies)
        );
    }

    @GetMapping("/related/{id}")
    public ResponseEntity<StandardResponse<List<MovieDTO>>> getRelatedMovies(@PathVariable Long id) {
        List<MovieDTO> movies = movieService.getRelatedMovies(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Related movies fetched successfully", movies)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<MovieDTO>> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO created = movieService.createMovie(movieDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Movie created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<MovieDTO>> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO updated = movieService.updateMovie(id, movieDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movie updated successfully", updated)
        );
    }

    @PatchMapping("/{id}/toggle-featured")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<MovieDTO>> toggleFeatured(@PathVariable Long id) {
        MovieDTO updated = movieService.toggleFeatured(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movie hero featured status toggled successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Movie deleted successfully", null)
        );
    }
}
