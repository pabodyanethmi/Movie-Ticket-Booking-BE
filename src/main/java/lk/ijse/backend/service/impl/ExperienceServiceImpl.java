package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.ExperienceDTO;
import lk.ijse.backend.entity.Experience;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.ExperienceRepository;
import lk.ijse.backend.service.ExperienceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDTO> getAllExperiences() {
        return experienceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceDTO getExperienceById(Long id) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        return mapToDTO(experience);
    }

    @Override
    @Transactional
    public ExperienceDTO createExperience(ExperienceDTO dto) {
        Experience experience = new Experience(
                dto.getTitle(),
                dto.getDescription(),
                dto.getIconUrl(),
                dto.getBannerImageUrl()
        );
        Experience saved = experienceRepository.save(experience);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public ExperienceDTO updateExperience(Long id, ExperienceDTO dto) {
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
        
        experience.setTitle(dto.getTitle());
        experience.setDescription(dto.getDescription());
        experience.setIconUrl(dto.getIconUrl());
        experience.setBannerImageUrl(dto.getBannerImageUrl());

        Experience updated = experienceRepository.save(experience);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience not found with id: " + id);
        }
        experienceRepository.deleteById(id);
    }

    private ExperienceDTO mapToDTO(Experience e) {
        return new ExperienceDTO(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getIconUrl(),
                e.getBannerImageUrl()
        );
    }
}
