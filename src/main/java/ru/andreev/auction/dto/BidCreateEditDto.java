package ru.andreev.auction.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
public class BidCreateEditDto {
    BigDecimal amount;

    LocalDateTime bidTime;

    Long userId;

    Long lotId;
}
