package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
