package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.TheaterDTO;
import lk.ijse.backend.entity.Theater;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.TheaterRepository;
import lk.ijse.backend.service.TheaterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterServiceImpl(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Override
    @Transactional
    public TheaterDTO createTheater(TheaterDTO theaterDTO) {
        Theater theater = Theater.builder()
                .name(theaterDTO.getName().trim())
                .location(theaterDTO.getLocation().trim())
                .totalScreens(theaterDTO.getTotalScreens() != null ? theaterDTO.getTotalScreens() : 1)
                .build();
        Theater saved = theaterRepository.save(theater);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TheaterDTO getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
        return mapToDTO(theater);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDTO> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TheaterDTO updateTheater(Long id, TheaterDTO theaterDTO) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));

        theater.setName(theaterDTO.getName().trim());
        theater.setLocation(theaterDTO.getLocation().trim());
        if (theaterDTO.getTotalScreens() != null) {
            theater.setTotalScreens(theaterDTO.getTotalScreens());
        }

        Theater updated = theaterRepository.save(theater);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteTheater(Long id) {
        if (!theaterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Theater not found with id: " + id);
        }
        theaterRepository.deleteById(id);
    }

    private TheaterDTO mapToDTO(Theater theater) {
        return TheaterDTO.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .totalScreens(theater.getTotalScreens())
                .build();
    }
}
