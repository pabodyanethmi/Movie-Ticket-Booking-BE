package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.PromotionDTO;
import lk.ijse.backend.service.PromotionService;
import lk.ijse.backend.util.StandardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    public ResponseEntity<StandardResponse<List<PromotionDTO>>> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "All promotions fetched successfully", promotions)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<StandardResponse<List<PromotionDTO>>> getActivePromotions() {
        List<PromotionDTO> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Active promotions fetched successfully", promotions)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse<PromotionDTO>> getPromotionById(@PathVariable Long id) {
        PromotionDTO promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Promotion details fetched successfully", promotion)
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<PromotionDTO>> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO created = promotionService.createPromotion(promotionDTO);
        return new ResponseEntity<>(
                new StandardResponse<>(HttpStatus.CREATED.value(), "Promotion created successfully", created),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<PromotionDTO>> updatePromotion(@PathVariable Long id, @Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionDTO updated = promotionService.updatePromotion(id, promotionDTO);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Promotion updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<StandardResponse<Void>> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Promotion deleted successfully", null)
        );
    }
}
