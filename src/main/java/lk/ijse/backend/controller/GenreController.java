package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.GenreDTO;
import lk.ijse.backend.service.GenreService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<GenreDTO>>> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Genres fetched successfully", genres)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<GenreDTO>> getGenreById(@PathVariable Long id) {
        GenreDTO genre = genreService.getGenreById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Genre fetched successfully", genre)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<GenreDTO>> createGenre(@Valid @RequestBody GenreDTO genreDTO) {
        GenreDTO created = genreService.createGenre(genreDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Genre created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<GenreDTO>> updateGenre(@PathVariable Long id, @Valid @RequestBody GenreDTO genreDTO) {
        GenreDTO updated = genreService.updateGenre(id, genreDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Genre updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Genre deleted successfully", null)
        );
    }
}
