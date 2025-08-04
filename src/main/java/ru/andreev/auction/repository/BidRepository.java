package ru.andreev.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andreev.auction.entity.Bid;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByLotIdOrderByAmountDesc(Long lotId);

    List<Bid> findAllByLot_Id(Long lotId);
}
