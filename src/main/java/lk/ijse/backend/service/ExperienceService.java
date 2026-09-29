package lk.ijse.backend.service;

import lk.ijse.backend.dto.ExperienceDTO;

import java.util.List;

public interface ExperienceService {
    List<ExperienceDTO> getAllExperiences();
    ExperienceDTO getExperienceById(Long id);
    ExperienceDTO createExperience(ExperienceDTO experienceDTO);
    ExperienceDTO updateExperience(Long id, ExperienceDTO experienceDTO);
    void deleteExperience(Long id);
}
