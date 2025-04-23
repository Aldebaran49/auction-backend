package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.Lot;

public interface LotRepository extends JpaRepository<Lot, Long> {
}
