package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.TheaterDTO;
import lk.ijse.backend.service.TheaterService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/theaters", "/api/v1/cinemas"})
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<TheaterDTO>>> getAllTheaters() {
        List<TheaterDTO> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Theaters fetched successfully", theaters)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<TheaterDTO>> getTheaterById(@PathVariable Long id) {
        TheaterDTO theater = theaterService.getTheaterById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Theater details fetched successfully", theater)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<TheaterDTO>> createTheater(@Valid @RequestBody TheaterDTO theaterDTO) {
        TheaterDTO created = theaterService.createTheater(theaterDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Theater created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<TheaterDTO>> updateTheater(@PathVariable Long id, @Valid @RequestBody TheaterDTO theaterDTO) {
        TheaterDTO updated = theaterService.updateTheater(id, theaterDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Theater updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Theater deleted successfully", null)
        );
    }
}
