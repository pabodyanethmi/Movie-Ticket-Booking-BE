package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByValidUntilAfterOrValidUntilIsNull(LocalDate date);
}
