package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.ScreenDTO;
import lk.ijse.backend.entity.Screen;
import lk.ijse.backend.entity.Theater;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.ScreenRepository;
import lk.ijse.backend.repository.TheaterRepository;
import lk.ijse.backend.service.ScreenService;
import lk.ijse.backend.service.SeatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;
    private final SeatService seatService;

    public ScreenServiceImpl(ScreenRepository screenRepository,
                             TheaterRepository theaterRepository,
                             SeatService seatService) {
        this.screenRepository = screenRepository;
        this.theaterRepository = theaterRepository;
        this.seatService = seatService;
    }

    @Override
    @Transactional
    public ScreenDTO createScreen(ScreenDTO screenDTO) {
        Theater theater = theaterRepository.findById(screenDTO.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screenDTO.getTheaterId()));

        Screen screen = Screen.builder()
                .theater(theater)
                .screenNumber(screenDTO.getScreenNumber())
                .seatCapacity(screenDTO.getSeatCapacity() != null ? screenDTO.getSeatCapacity() : 60)
                .build();

        Screen saved = screenRepository.save(screen);
        seatService.generateSeatsForScreen(saved.getId(), 5, 12);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenDTO getScreenById(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));
        return mapToDTO(screen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScreenDTO> getScreensByTheaterId(Long theaterId) {
        return screenRepository.findByTheaterId(theaterId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScreenDTO updateScreen(Long id, ScreenDTO screenDTO) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + id));

        screen.setScreenNumber(screenDTO.getScreenNumber());
        if (screenDTO.getSeatCapacity() != null) {
            screen.setSeatCapacity(screenDTO.getSeatCapacity());
        }

        Screen updated = screenRepository.save(screen);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteScreen(Long id) {
        if (!screenRepository.existsById(id)) {
            throw new ResourceNotFoundException("Screen not found with id: " + id);
        }
        screenRepository.deleteById(id);
    }

    private ScreenDTO mapToDTO(Screen screen) {
        return ScreenDTO.builder()
                .id(screen.getId())
                .theaterId(screen.getTheater().getId())
                .theaterName(screen.getTheater().getName())
                .screenNumber(screen.getScreenNumber())
                .seatCapacity(screen.getSeatCapacity())
                .build();
    }
}
