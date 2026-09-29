package lk.ijse.backend.service;

import lk.ijse.backend.dto.PromotionDTO;

import java.util.List;

public interface PromotionService {
    List<PromotionDTO> getAllPromotions();
    List<PromotionDTO> getActivePromotions();
    PromotionDTO getPromotionById(Long id);
    PromotionDTO createPromotion(PromotionDTO promotionDTO);
    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);
    void deletePromotion(Long id);
}
