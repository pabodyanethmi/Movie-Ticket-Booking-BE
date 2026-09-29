package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.ScreenDTO;
import lk.ijse.backend.service.ScreenService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<StandardResponse<List<ScreenDTO>>> getScreensByTheaterId(@PathVariable Long theaterId) {
        List<ScreenDTO> screens = screenService.getScreensByTheaterId(theaterId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Screens fetched successfully", screens)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ScreenDTO>> getScreenById(@PathVariable Long id) {
        ScreenDTO screen = screenService.getScreenById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Screen details fetched successfully", screen)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ScreenDTO>> createScreen(@Valid @RequestBody ScreenDTO screenDTO) {
        ScreenDTO created = screenService.createScreen(screenDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Screen created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ScreenDTO>> updateScreen(@PathVariable Long id, @Valid @RequestBody ScreenDTO screenDTO) {
        ScreenDTO updated = screenService.updateScreen(id, screenDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Screen updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Screen deleted successfully", null)
        );
    }
}
