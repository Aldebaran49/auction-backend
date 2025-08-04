package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.LotStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findAllByStatusAndExpiredAtBefore(LotStatus status, LocalDateTime now);
    List<Lot> findAllByStatusAndStartedAtBefore(LotStatus status, LocalDateTime now);

}
