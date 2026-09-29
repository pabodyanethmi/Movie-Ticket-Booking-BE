package lk.ijse.backend.service;

import lk.ijse.backend.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    GenreDTO createGenre(GenreDTO genreDTO);
    GenreDTO getGenreById(Long id);
    List<GenreDTO> getAllGenres();
    GenreDTO updateGenre(Long id, GenreDTO genreDTO);
    void deleteGenre(Long id);
}
