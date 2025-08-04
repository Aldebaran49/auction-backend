package ru.andreev.auction.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class BidCreateTransferDto {
    BigDecimal amount;

    Long lotId;
}
