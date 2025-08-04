package ru.andreev.auction.exception;

public class AuctionInvalidBidException extends RuntimeException {
    public AuctionInvalidBidException(String msg) {
        super(msg);
    }
}
