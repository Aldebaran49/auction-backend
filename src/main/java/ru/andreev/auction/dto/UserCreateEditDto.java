package ru.andreev.auction.dto;

import lombok.Value;
import ru.andreev.auction.entity.Role;

import java.util.List;

@Value
public class UserCreateEditDto {
    String username;

    String password;

    String email;

    Role role;
}
