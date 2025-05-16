package ru.andreev.auction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
