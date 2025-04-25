package ru.andreev.auction.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class LotCreateEditDto {
    String title;
    String description;
    BigDecimal price;
    LocalDateTime expiredAt;
}
