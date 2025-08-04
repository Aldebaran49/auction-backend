package ru.andreev.auction.mapper;

import org.springframework.stereotype.Component;
import ru.andreev.auction.dto.RegisterRequestDto;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.entity.Role;
import ru.andreev.auction.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class RegisterMapper implements Mapper<RegisterRequestDto, User>{
    @Override
    public User map(RegisterRequestDto requestDto) {
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(requestDto.getPassword());
        user.setEmail(requestDto.getEmail());
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
