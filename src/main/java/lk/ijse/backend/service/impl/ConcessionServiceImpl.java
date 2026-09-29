package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.ConcessionDTO;
import lk.ijse.backend.entity.Concession;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.ConcessionRepository;
import lk.ijse.backend.service.ConcessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcessionServiceImpl implements ConcessionService {

    private final ConcessionRepository concessionRepository;

    public ConcessionServiceImpl(ConcessionRepository concessionRepository) {
        this.concessionRepository = concessionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConcessionDTO> getAllConcessions(String category, boolean includeAll) {
        List<Concession> list;
        boolean hasCategory = StringUtils.hasText(category) && !"ALL".equalsIgnoreCase(category.trim());

        if (includeAll) {
            if (hasCategory) {
                list = concessionRepository.findByCategoryIgnoreCase(category.trim());
            } else {
                list = concessionRepository.findAll();
            }
        } else {
            if (hasCategory) {
                list = concessionRepository.findByCategoryIgnoreCaseAndIsAvailableTrue(category.trim());
            } else {
                list = concessionRepository.findByIsAvailableTrue();
            }
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ConcessionDTO getConcessionById(Long id) {
        Concession concession = concessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concession not found with id: " + id));
        return mapToDTO(concession);
    }

    @Override
    @Transactional
    public ConcessionDTO createConcession(ConcessionDTO concessionDTO) {
        Concession concession = mapToEntity(concessionDTO);
        Concession saved = concessionRepository.save(concession);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ConcessionDTO updateConcession(Long id, ConcessionDTO concessionDTO) {
        Concession concession = concessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concession not found with id: " + id));

        if (concessionDTO.getName() != null) concession.setName(concessionDTO.getName());
        if (concessionDTO.getCategory() != null) concession.setCategory(concessionDTO.getCategory());
        if (concessionDTO.getPrice() != null) concession.setPrice(concessionDTO.getPrice());
        if (concessionDTO.getImageUrl() != null) concession.setImageUrl(concessionDTO.getImageUrl());
        if (concessionDTO.getDescription() != null) concession.setDescription(concessionDTO.getDescription());
        if (concessionDTO.getIsAvailable() != null) concession.setIsAvailable(concessionDTO.getIsAvailable());

        Concession updated = concessionRepository.save(concession);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public ConcessionDTO toggleAvailability(Long id) {
        Concession concession = concessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concession not found with id: " + id));

        boolean currentStatus = concession.getIsAvailable() != null ? concession.getIsAvailable() : true;
        concession.setIsAvailable(!currentStatus);

        Concession updated = concessionRepository.save(concession);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteConcession(Long id) {
        Concession concession = concessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concession not found with id: " + id));
        concessionRepository.delete(concession);
    }

    private ConcessionDTO mapToDTO(Concession entity) {
        return ConcessionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .isAvailable(entity.getIsAvailable())
                .build();
    }

    private Concession mapToEntity(ConcessionDTO dto) {
        return Concession.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .build();
    }
}
