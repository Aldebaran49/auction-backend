package ru.andreev.auction.dto;


import lombok.Value;

@Value
public class AuthRequestDto {
    String username;
    String password;
}
