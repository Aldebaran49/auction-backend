package ru.andreev.auction.controller;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("")
    public List<UserReadDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("")
    public UserReadDto create(UserCreateEditDto dto) {
        return userService.create(dto);
    }

    @PutMapping("")
    public UserReadDto update(Long id, UserCreateEditDto dto) {
        return userService.update(dto, id).orElseThrow();
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean status = userService.delete(id);
        if (!status) {

        }
    }
}
