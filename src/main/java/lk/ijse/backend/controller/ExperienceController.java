package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.ExperienceDTO;
import lk.ijse.backend.service.ExperienceService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<ExperienceDTO>>> getAllExperiences() {
        List<ExperienceDTO> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Experiences fetched successfully", experiences)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<ExperienceDTO>> getExperienceById(@PathVariable Long id) {
        ExperienceDTO experience = experienceService.getExperienceById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Experience details fetched successfully", experience)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ExperienceDTO>> createExperience(@Valid @RequestBody ExperienceDTO experienceDTO) {
        ExperienceDTO created = experienceService.createExperience(experienceDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Experience created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<ExperienceDTO>> updateExperience(@PathVariable Long id, @Valid @RequestBody ExperienceDTO experienceDTO) {
        ExperienceDTO updated = experienceService.updateExperience(id, experienceDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Experience updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Experience deleted successfully", null)
        );
    }
}
