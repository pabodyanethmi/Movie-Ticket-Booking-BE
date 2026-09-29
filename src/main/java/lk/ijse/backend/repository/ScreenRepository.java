package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheaterId(Long theaterId);
    Optional<Screen> findByTheaterIdAndScreenNumber(Long theaterId, Integer screenNumber);
}
