package ru.andreev.auction.service;

import org.springframework.stereotype.Service;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.mapper.UserReadMapper;
import ru.andreev.auction.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;
    UserReadMapper userReadMapper;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }
    public UserReadDto findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map).orElse(null);
    }
    public UserReadDto create(UserCreateEditDto user) {
        return userRepository.;
    }
    public Optional<UserReadDto> update(UserCreateEditDto user, Long id) {
        return null;
    }
    public boolean delete(Long id) {
        return false;
    }
}
