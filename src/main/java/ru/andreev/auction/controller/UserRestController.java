package ru.andreev.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    UserService userService;

    @GetMapping("")
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    public UserReadDto create(UserCreateEditDto dto) {
        return userService.create(dto);
    }
}
