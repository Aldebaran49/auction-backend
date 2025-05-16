package ru.andreev.auction.controller;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserReadDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<UserReadDto> create(@Valid UserCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @PutMapping("")
    public ResponseEntity<UserReadDto> update(Long id, @Valid UserCreateEditDto dto) {
        Optional<UserReadDto> response = userService.update(id, dto);
        return response.map(user -> ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(user)).orElseGet(() -> ResponseEntity
                .badRequest().body(null));
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean status = userService.delete(id);
    }
}
