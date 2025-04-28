package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.BidCreateEditDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.repository.LotRepository;
import ru.andreev.auction.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class BidCreateEditMapper implements Mapper<BidCreateEditDto, Bid> {
    private final LotRepository lotRepository;
    private final UserRepository userRepository;

    @Override
    public Bid map(BidCreateEditDto dto) {
        Bid bid = new Bid();
        copy(dto, bid);
        return bid;
    }

    @Override
    public Bid map(BidCreateEditDto fromObject, Bid toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    public void copy(BidCreateEditDto dto, Bid bid) {
        bid.setBidTime(dto.getBidTime());
        bid.setAmount(dto.getAmount());
        bid.setLot(lotRepository.findById(dto.getLotId()).orElse(null));
        bid.setUser(userRepository.findById(dto.getUserId()).orElse(null));
    }
}
