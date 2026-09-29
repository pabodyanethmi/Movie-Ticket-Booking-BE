package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.ConcessionDTO;
import lk.ijse.backend.service.ConcessionService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concessions")
@CrossOrigin(origins = "*")
public class ConcessionController {

    private final ConcessionService concessionService;

    public ConcessionController(ConcessionService concessionService) {
        this.concessionService = concessionService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<ConcessionDTO>>> getConcessions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") boolean includeAll) {
        List<ConcessionDTO> concessions = concessionService.getAllConcessions(category, includeAll);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Concessions fetched successfully", concessions)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ConcessionDTO>> getConcessionById(@PathVariable Long id) {
        ConcessionDTO concession = concessionService.getConcessionById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Concession details fetched successfully", concession)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ConcessionDTO>> createConcession(@Valid @RequestBody ConcessionDTO concessionDTO) {
        ConcessionDTO created = concessionService.createConcession(concessionDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Concession created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ConcessionDTO>> updateConcession(
            @PathVariable Long id, @Valid @RequestBody ConcessionDTO concessionDTO) {
        ConcessionDTO updated = concessionService.updateConcession(id, concessionDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Concession updated successfully", updated)
        );
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ConcessionDTO>> toggleAvailability(@PathVariable Long id) {
        ConcessionDTO updated = concessionService.toggleAvailability(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Concession availability toggled successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteConcession(@PathVariable Long id) {
        concessionService.deleteConcession(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Concession deleted successfully", null)
        );
    }
}
