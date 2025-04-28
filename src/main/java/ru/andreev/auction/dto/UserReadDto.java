package ru.andreev.auction.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Value;
import ru.andreev.auction.entity.Role;

import java.util.List;

@Value
public class UserReadDto {
    Long id;

    String username;

    String password;

    String email;

    Role role;

    List<Long> bidIDs;
}
