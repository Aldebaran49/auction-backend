package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.entity.User;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto>{
    @Override
    public UserReadDto map(User user) {
        return new UserReadDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                user.getBids().stream().map(Bid::getId).toList()
        );
    }
}
