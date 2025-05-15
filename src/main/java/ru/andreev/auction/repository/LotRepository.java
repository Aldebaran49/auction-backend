package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.Lot;

import java.time.LocalDateTime;
import java.util.List;

public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findByExpiredAtAfter(LocalDateTime now);
    List<Lot> findByExpiredAtBefore(LocalDateTime now);

}
