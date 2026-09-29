package lk.ijse.backend.service;

import lk.ijse.backend.dto.BulkShowRequestDTO;
import lk.ijse.backend.dto.ShowDTO;
import lk.ijse.backend.dto.ShowDetailDTO;

import java.util.List;

public interface ShowService {
    ShowDTO createShow(ShowDTO showDTO);
    List<ShowDTO> createBulkShows(BulkShowRequestDTO bulkRequest);
    ShowDTO getShowById(Long id);
    ShowDetailDTO getShowDetailsById(Long id);
    List<ShowDTO> getAllShows();
    List<ShowDTO> getShowsByMovieId(Long movieId);
    List<ShowDTO> getUpcomingShowsByMovieId(Long movieId);
    List<ShowDTO> getShowsByScreenId(Long screenId);
    ShowDTO updateShow(Long id, ShowDTO showDTO);
    void deleteShow(Long id);
}
