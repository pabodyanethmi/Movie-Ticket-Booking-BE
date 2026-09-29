package lk.ijse.backend.service;

import lk.ijse.backend.dto.MovieDTO;
import lk.ijse.backend.entity.MovieStatus;

import java.util.List;

public interface MovieService {
    MovieDTO createMovie(MovieDTO movieDTO);
    MovieDTO getMovieById(Long id);
    List<MovieDTO> getAllMovies();
    List<MovieDTO> searchMovies(String query);
    List<MovieDTO> getMoviesByGenre(String genre);
    List<MovieDTO> getNowShowingMovies();
    List<MovieDTO> getFeaturedMovies();
    List<MovieDTO> getMoviesByStatus(MovieStatus status);
    List<MovieDTO> getRelatedMovies(Long id);
    MovieDTO updateMovie(Long id, MovieDTO movieDTO);
    MovieDTO toggleFeatured(Long id);
    void deleteMovie(Long id);
}
