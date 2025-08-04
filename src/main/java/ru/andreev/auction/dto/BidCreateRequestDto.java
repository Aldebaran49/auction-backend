package ru.andreev.auction.dto;

import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class BidCreateRequestDto {
    @Positive
    BigDecimal amount;
}
