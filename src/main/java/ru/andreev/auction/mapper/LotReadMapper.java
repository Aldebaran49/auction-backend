package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.entity.Lot;

@Component
@RequiredArgsConstructor
public class LotReadMapper implements Mapper<Lot, LotReadDto> {
    private final BidReadMapper bidReadMapper;
    @Override
    public LotReadDto map(Lot lot) {
        return new LotReadDto(
                lot.getId(),
                lot.getTitle(),
                lot.getDescription(),
                lot.getPrice(),
                lot.getCreatedAt(),
                lot.getExpiredAt(),
                lot.getBids().stream().map(Bid::getId).toList()
        );
    }
}
