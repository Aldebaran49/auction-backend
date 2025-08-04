package ru.andreev.auction.dto;

import lombok.Value;
import ru.andreev.auction.entity.LotStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class LotReadDto {
    Long id;
    String title;
    String description;
    BigDecimal startPrice;
    BigDecimal currentPrice;
    BigDecimal bidStep;
    BigDecimal blitzPrice;
    LocalDateTime createdAt;
    LocalDateTime startedAt;
    LocalDateTime expiredAt;
    LotStatus status;
    List<Long> bidIDs;
    Long ownerID;
    Long winnerID;
}
