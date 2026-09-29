package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.SeatDTO;
import lk.ijse.backend.service.SeatService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/screen/{screenId}")
    public ResponseEntity<StandardResponse<List<SeatDTO>>> getSeatsByScreenId(@PathVariable Long screenId) {
        List<SeatDTO> seats = seatService.getSeatsByScreenId(screenId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Seats fetched successfully", seats)
        );
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<StandardResponse<List<SeatDTO>>> getSeatsByShowId(@PathVariable Long showId) {
        List<SeatDTO> seats = seatService.getSeatsByShowId(showId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Show seat layout and availability fetched successfully", seats)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<SeatDTO>> createSeat(@Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO created = seatService.createSeat(seatDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Seat created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/generate/screen/{screenId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<String>> generateSeats(
            @PathVariable Long screenId,
            @RequestParam(defaultValue = "5") int rows,
            @RequestParam(defaultValue = "12") int seatsPerRow) {
        seatService.generateSeatsForScreen(screenId, rows, seatsPerRow);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Seats generated successfully", "Layout: " + rows + " rows x " + seatsPerRow + " seats")
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Seat deleted successfully", null)
        );
    }
}
