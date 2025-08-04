package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.BidReadDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.repository.LotRepository;
import ru.andreev.auction.repository.UserRepository;

@Component
public class BidReadMapper implements Mapper<Bid, BidReadDto> {
    UserRepository userRepository;
    LotRepository lotRepository;
    @Override
    public BidReadDto map(Bid bid) {
        return new BidReadDto(
                bid.getId(),
                bid.getAmount(),
                bid.getBidTime(),
                bid.getOwner().getId(),
                bid.getLot().getId()
        );
    }
}
