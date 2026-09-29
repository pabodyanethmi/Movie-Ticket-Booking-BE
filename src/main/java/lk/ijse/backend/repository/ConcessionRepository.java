package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Concession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcessionRepository extends JpaRepository<Concession, Long> {
    List<Concession> findByIsAvailableTrue();
    List<Concession> findByCategoryIgnoreCaseAndIsAvailableTrue(String category);
    List<Concession> findByCategoryIgnoreCase(String category);
}
