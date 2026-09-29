package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.SeatDTO;
import lk.ijse.backend.entity.Screen;
import lk.ijse.backend.entity.Seat;
import lk.ijse.backend.entity.Show;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.BookingSeatRepository;
import lk.ijse.backend.repository.ScreenRepository;
import lk.ijse.backend.repository.SeatRepository;
import lk.ijse.backend.repository.ShowRepository;
import lk.ijse.backend.service.SeatService;
import lk.ijse.backend.util.SeatType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreenRepository screenRepository;
    private final ShowRepository showRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public SeatServiceImpl(SeatRepository seatRepository,
                           ScreenRepository screenRepository,
                           ShowRepository showRepository,
                           BookingSeatRepository bookingSeatRepository) {
        this.seatRepository = seatRepository;
        this.screenRepository = screenRepository;
        this.showRepository = showRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @Override
    @Transactional
    public SeatDTO createSeat(SeatDTO seatDTO) {
        Screen screen = screenRepository.findById(seatDTO.getScreenId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + seatDTO.getScreenId()));

        Seat seat = Seat.builder()
                .screen(screen)
                .seatRow(seatDTO.getSeatRow().toUpperCase().trim())
                .seatNumber(seatDTO.getSeatNumber())
                .seatType(seatDTO.getSeatType() != null ? seatDTO.getSeatType() : SeatType.STANDARD)
                .build();

        Seat saved = seatRepository.save(seat);
        return mapToDTO(saved, false, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDTO> getSeatsByScreenId(Long screenId) {
        return seatRepository.findByScreenIdOrderBySeatRowAscSeatNumberAsc(screenId).stream()
                .map(seat -> mapToDTO(seat, false, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeatDTO> getSeatsByShowId(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + showId));

        Long screenId = show.getScreen().getId();
        List<Seat> seats = seatRepository.findByScreenIdOrderBySeatRowAscSeatNumberAsc(screenId);
        List<Long> bookedSeatIds = bookingSeatRepository.findBookedSeatIdsByShowId(showId);
        Set<Long> bookedSeatIdSet = new HashSet<>(bookedSeatIds);

        return seats.stream()
                .map(seat -> {
                    boolean isBooked = bookedSeatIdSet.contains(seat.getId());
                    double calculatedPrice = calculatePrice(show.getTicketPrice(), seat.getSeatType());
                    return mapToDTO(seat, isBooked, calculatedPrice);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void generateSeatsForScreen(Long screenId, int rows, int seatsPerRow) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found with id: " + screenId));

        List<Seat> existingSeats = seatRepository.findByScreenIdOrderBySeatRowAscSeatNumberAsc(screenId);
        if (!existingSeats.isEmpty()) {
            return;
        }

        List<Seat> seatsToSave = new ArrayList<>();
        char rowChar = 'A';

        for (int r = 0; r < rows; r++) {
            String row = String.valueOf((char) (rowChar + r));
            SeatType seatType;
            if (r == 0 || r == 1) {
                seatType = SeatType.STANDARD;
            } else if (r >= rows - 2) {
                seatType = SeatType.VIP;
            } else {
                seatType = SeatType.PREMIUM;
            }

            for (int num = 1; num <= seatsPerRow; num++) {
                Seat seat = Seat.builder()
                        .screen(screen)
                        .seatRow(row)
                        .seatNumber(num)
                        .seatType(seatType)
                        .build();
                seatsToSave.add(seat);
            }
        }

        seatRepository.saveAll(seatsToSave);
    }

    @Override
    @Transactional
    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    private double calculatePrice(Double basePrice, SeatType seatType) {
        if (basePrice == null) basePrice = 10.0;
        if (seatType == SeatType.VIP) {
            return basePrice * 1.5;
        } else if (seatType == SeatType.PREMIUM) {
            return basePrice * 1.25;
        }
        return basePrice;
    }

    private SeatDTO mapToDTO(Seat seat, boolean isBooked, Double price) {
        return SeatDTO.builder()
                .id(seat.getId())
                .screenId(seat.getScreen().getId())
                .seatRow(seat.getSeatRow())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .isBooked(isBooked)
                .price(price)
                .build();
    }
}
