package lk.ijse.backend.service;

import lk.ijse.backend.dto.SeatDTO;

import java.util.List;

public interface SeatService {
    SeatDTO createSeat(SeatDTO seatDTO);
    List<SeatDTO> getSeatsByScreenId(Long screenId);
    List<SeatDTO> getSeatsByShowId(Long showId);
    void generateSeatsForScreen(Long screenId, int rows, int seatsPerRow);
    void deleteSeat(Long id);
}
