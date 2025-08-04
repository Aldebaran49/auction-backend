package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.LotStatus;

import java.time.LocalDateTime;

@Component
public class LotCreateEditMapper implements Mapper<LotCreateEditDto, Lot> {
    @Override
    public Lot map(LotCreateEditDto from) {
        Lot to = new Lot();
        copy(from, to);
        return to;
    }

    @Override
    public Lot map(LotCreateEditDto from, Lot to) {
        copy(from, to);
        return to;
    }

    public void copy (LotCreateEditDto from, Lot to) {
        to.setCreatedAt(LocalDateTime.now());
        to.setStartedAt(from.getStartedAt());
        to.setExpiredAt(from.getExpiredAt());
        to.setDescription(from.getDescription());
        to.setTitle(from.getTitle());
        to.setStartPrice(from.getStartPrice());
        to.setCurrentPrice(from.getStartPrice());
        to.setBidStep(from.getBidStep());
        to.setBlitzPrice(from.getBlitzPrice());
        to.setStatus(LotStatus.WAITING);
    }

}
