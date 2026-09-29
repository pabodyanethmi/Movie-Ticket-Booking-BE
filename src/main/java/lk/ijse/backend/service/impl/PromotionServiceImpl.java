package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.PromotionDTO;
import lk.ijse.backend.entity.Promotion;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.PromotionRepository;
import lk.ijse.backend.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionDTO> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionDTO> getActivePromotions() {
        return promotionRepository.findByValidUntilAfterOrValidUntilIsNull(LocalDate.now()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionDTO getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));
        return mapToDTO(promotion);
    }

    @Override
    @Transactional
    public PromotionDTO createPromotion(PromotionDTO dto) {
        Promotion p = new Promotion(
                dto.getTitle(),
                dto.getSubtitle(),
                dto.getDiscountPercent(),
                dto.getDescription(),
                dto.getBannerUrl(),
                dto.getValidUntil(),
                dto.getTermsUrl()
        );
        Promotion saved = promotionRepository.save(p);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public PromotionDTO updatePromotion(Long id, PromotionDTO dto) {
        Promotion p = promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));

        p.setTitle(dto.getTitle());
        p.setSubtitle(dto.getSubtitle());
        p.setDiscountPercent(dto.getDiscountPercent());
        p.setDescription(dto.getDescription());
        p.setBannerUrl(dto.getBannerUrl());
        p.setValidUntil(dto.getValidUntil());
        p.setTermsUrl(dto.getTermsUrl());

        Promotion updated = promotionRepository.save(p);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion not found with id: " + id);
        }
        promotionRepository.deleteById(id);
    }

    private PromotionDTO mapToDTO(Promotion p) {
        return new PromotionDTO(
                p.getId(),
                p.getTitle(),
                p.getSubtitle(),
                p.getDiscountPercent(),
                p.getDescription(),
                p.getBannerUrl(),
                p.getValidUntil(),
                p.getTermsUrl()
        );
    }
}
