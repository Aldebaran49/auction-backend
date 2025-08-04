package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.andreev.auction.dto.AuthRequestDto;
import ru.andreev.auction.dto.AuthResponseDto;
import ru.andreev.auction.dto.RegisterRequestDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.entity.UserDetailsImpl;
import ru.andreev.auction.jwt.JwtUtil;
import ru.andreev.auction.mapper.RegisterMapper;
import ru.andreev.auction.mapper.UserReadMapper;
import ru.andreev.auction.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RegisterMapper registerMapper;
    private final UserReadMapper userReadMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserReadDto register(RegisterRequestDto registerRequestDto) {
        return Optional.of(registerRequestDto)
                .map(registerMapper::map)
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    return u;
                })
                .map(userRepository::save)
                .map(userReadMapper::map)
                .orElseThrow();
    }

    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        UserDetails userDetails = Optional
                .of(userRepository.findByUsername(authRequestDto.getUsername()))
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new RuntimeException("UserNotFound"));

        if (!passwordEncoder.matches(authRequestDto.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponseDto(token);
    }
}
