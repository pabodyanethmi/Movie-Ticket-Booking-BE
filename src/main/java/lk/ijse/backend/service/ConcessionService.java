package lk.ijse.backend.service;

import lk.ijse.backend.dto.ConcessionDTO;

import java.util.List;

public interface ConcessionService {
    List<ConcessionDTO> getAllConcessions(String category, boolean includeAll);
    ConcessionDTO getConcessionById(Long id);
    ConcessionDTO createConcession(ConcessionDTO concessionDTO);
    ConcessionDTO updateConcession(Long id, ConcessionDTO concessionDTO);
    ConcessionDTO toggleAvailability(Long id);
    void deleteConcession(Long id);
}
