package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.ShowDTO;
import lk.ijse.backend.dto.ShowDetailDTO;
import lk.ijse.backend.service.ShowService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<ShowDTO>>> getAllShows(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long screenId) {
        List<ShowDTO> shows;
        if (movieId != null) {
            shows = showService.getShowsByMovieId(movieId);
        } else if (screenId != null) {
            shows = showService.getShowsByScreenId(screenId);
        } else {
            shows = showService.getAllShows();
        }
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Shows fetched successfully", shows)
        );
    }

    @GetMapping("/movie/{movieId}/upcoming")
    public ResponseEntity<StandardResponse<List<ShowDTO>>> getUpcomingShowsByMovie(@PathVariable Long movieId) {
        List<ShowDTO> shows = showService.getUpcomingShowsByMovieId(movieId);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Upcoming shows fetched successfully", shows)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ShowDTO>> getShowById(@PathVariable Long id) {
        ShowDTO show = showService.getShowById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Show details fetched successfully", show)
        );
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<StandardResponse<ShowDetailDTO>> getShowDetailsById(@PathVariable Long id) {
        ShowDetailDTO showDetails = showService.getShowDetailsById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Show details with seat layout fetched successfully", showDetails)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ShowDTO>> createShow(@Valid @RequestBody ShowDTO showDTO) {
        ShowDTO created = showService.createShow(showDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Show scheduled successfully", created),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<List<ShowDTO>>> createBulkShows(@Valid @RequestBody lk.ijse.backend.dto.BulkShowRequestDTO bulkRequest) {
        List<ShowDTO> created = showService.createBulkShows(bulkRequest);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Bulk showtimes generated successfully (" + created.size() + " shows created)", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ShowDTO>> updateShow(@PathVariable Long id, @Valid @RequestBody ShowDTO showDTO) {
        ShowDTO updated = showService.updateShow(id, showDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Show updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Show deleted successfully", null)
        );
    }
}
