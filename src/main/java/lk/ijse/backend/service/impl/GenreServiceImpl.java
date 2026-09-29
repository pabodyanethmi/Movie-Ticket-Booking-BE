package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.GenreDTO;
import lk.ijse.backend.entity.Genre;
import lk.ijse.backend.exception.DuplicateResourceException;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.GenreRepository;
import lk.ijse.backend.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public GenreDTO createGenre(GenreDTO genreDTO) {
        if (genreRepository.existsByNameIgnoreCase(genreDTO.getName().trim())) {
            throw new DuplicateResourceException("Genre already exists with name: " + genreDTO.getName());
        }
        Genre genre = Genre.builder()
                .name(genreDTO.getName().trim())
                .build();
        Genre saved = genreRepository.save(genre);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
        return mapToDTO(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        genre.setName(genreDTO.getName().trim());
        Genre updated = genreRepository.save(genre);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }

    private GenreDTO mapToDTO(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
