package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.User;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LotReadMapper implements Mapper<Lot, LotReadDto> {
    @Override
    public LotReadDto map(Lot lot) {
        return new LotReadDto(
                lot.getId(),
                lot.getTitle(),
                lot.getDescription(),
                lot.getStartPrice(),
                lot.getCurrentPrice(),
                lot.getBidStep(),
                lot.getBlitzPrice(),
                lot.getCreatedAt(),
                lot.getStartedAt(),
                lot.getExpiredAt(),
                lot.getStatus(),
                lot.getBids().stream().map(Bid::getId).toList(),
                lot.getOwner().getId(),
                Optional.ofNullable(lot.getWinner()).map(User::getId).orElse(null)
        );
    }
}
