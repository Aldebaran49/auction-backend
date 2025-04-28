package ru.andreev.auction.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Value;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Value
public class BidReadDto {
    Long id;

    BigDecimal amount;

    LocalDateTime bidTime;

    Long userId;

    Long lotId;
}
