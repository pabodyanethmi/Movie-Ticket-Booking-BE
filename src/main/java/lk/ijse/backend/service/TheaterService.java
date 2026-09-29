package lk.ijse.backend.service;

import lk.ijse.backend.dto.TheaterDTO;

import java.util.List;

public interface TheaterService {
    TheaterDTO createTheater(TheaterDTO theaterDTO);
    TheaterDTO getTheaterById(Long id);
    List<TheaterDTO> getAllTheaters();
    TheaterDTO updateTheater(Long id, TheaterDTO theaterDTO);
    void deleteTheater(Long id);
}
