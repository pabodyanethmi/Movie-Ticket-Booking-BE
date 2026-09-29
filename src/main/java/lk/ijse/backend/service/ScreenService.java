package lk.ijse.backend.service;

import lk.ijse.backend.dto.ScreenDTO;

import java.util.List;

public interface ScreenService {
    ScreenDTO createScreen(ScreenDTO screenDTO);
    ScreenDTO getScreenById(Long id);
    List<ScreenDTO> getScreensByTheaterId(Long theaterId);
    ScreenDTO updateScreen(Long id, ScreenDTO screenDTO);
    void deleteScreen(Long id);
}
