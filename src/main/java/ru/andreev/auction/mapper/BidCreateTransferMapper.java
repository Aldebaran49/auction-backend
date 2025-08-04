package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.BidCreateTransferDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.repository.LotRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BidCreateTransferMapper implements Mapper<BidCreateTransferDto, Bid> {
    private final LotRepository lotRepository;

    @Override
    public Bid map(BidCreateTransferDto dto) {
        Bid bid = new Bid();
        copy(dto, bid);
        return bid;
    }

    @Override
    public Bid map(BidCreateTransferDto fromObject, Bid toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    public void copy(BidCreateTransferDto dto, Bid bid) {
        bid.setBidTime(LocalDateTime.now());
        bid.setAmount(dto.getAmount());
        //bid.setLot(lotRepository.findById(dto.getLotId()).orElse(null));
        //bid.setUser(userRepository.findById(dto.getUserId()).orElse(null));
    }
}
