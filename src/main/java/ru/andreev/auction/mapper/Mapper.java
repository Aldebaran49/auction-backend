package ru.andreev.auction.mapper;

public interface Mapper <F, T> {
    T map (F object);
}
