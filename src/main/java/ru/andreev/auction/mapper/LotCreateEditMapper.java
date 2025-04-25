package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.entity.Lot;

import java.time.LocalDateTime;

@Component
public class LotCreateEditMapper implements Mapper<LotCreateEditDto, Lot> {
    @Override
    public Lot map(LotCreateEditDto object) {
        Lot lot = new Lot();
        copy(object, lot);
        return lot;
    }

    @Override
    public Lot map(LotCreateEditDto fromObject, Lot toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    public void copy (LotCreateEditDto from, Lot to) {
        to.setCreatedAt(LocalDateTime.now());
        to.setExpiredAt(from.getExpiredAt());
        to.setDescription(from.getDescription());
        to.setTitle(from.getTitle());
        to.setPrice(from.getPrice());
    }
}
