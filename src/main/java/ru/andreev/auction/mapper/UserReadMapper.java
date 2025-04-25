package ru.andreev.auction.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.entity.User;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto>{
    private final BidReadMapper bidReadMapper;
    @Override
    public UserReadDto map(User user) {
        UserReadDto dto = new UserReadDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                user.getBids().stream().map(bidReadMapper::map).toList()
        );
        return dto;
    }
}
