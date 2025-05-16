package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.mapper.UserCreateEditMapper;
import ru.andreev.auction.mapper.UserReadMapper;
import ru.andreev.auction.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream().map(userReadMapper::map).toList();
    }
    public UserReadDto findById(Long id) {
        return userRepository.findById(id).map(userReadMapper::map).orElse(null);
    }
    @Transactional
    public UserReadDto create(UserCreateEditDto user) {
        System.out.println("!!!");
        return Optional.of(user)
                .map(userCreateEditMapper::map)
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }
    @Transactional
    public Optional<UserReadDto> update(Long id, UserCreateEditDto dto) {
        return userRepository.findById(id).map(u -> userCreateEditMapper.map(dto, u))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::map);
    }
    @Transactional
    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(e -> {
                    userRepository.delete(e);
                    userRepository.flush();
                    return true;
                }).orElse(false);
    }
}
