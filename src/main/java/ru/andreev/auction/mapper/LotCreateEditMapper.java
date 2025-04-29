package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.entity.Lot;

import java.time.LocalDateTime;

@Component
public class LotCreateEditMapper implements Mapper<LotCreateEditDto, Lot> {
    @Override
    public Lot map(LotCreateEditDto from) {
        Lot to = new Lot();
        to.setCreatedAt(LocalDateTime.now());
        to.setExpiredAt(from.getExpiredAt());
        to.setDescription(from.getDescription());
        to.setTitle(from.getTitle());
        to.setPrice(from.getPrice());
        return to;
    }

    @Override
    public Lot map(LotCreateEditDto from, Lot to) {
        to.setCreatedAt(from.getCreatedAt());
        to.setExpiredAt(from.getExpiredAt());
        to.setDescription(from.getDescription());
        to.setTitle(from.getTitle());
        to.setPrice(from.getPrice());
        return to;
    }

}
