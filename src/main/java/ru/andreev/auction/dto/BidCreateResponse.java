package ru.andreev.auction.dto;

import lombok.Value;

@Value
public class BidCreateResponse {
    BidReadDto dto;
    boolean status;
    String message;
}
