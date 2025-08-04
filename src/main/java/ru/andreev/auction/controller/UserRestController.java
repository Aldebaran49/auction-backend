package ru.andreev.auction.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
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
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<UserReadDto> create(@Valid @RequestBody UserCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserReadDto> update(@PathVariable("id") Long id,
                                              @Valid @RequestBody UserCreateEditDto dto) {
        return userService.update(id, dto)
                .map(user -> ResponseEntity.status(HttpStatus.OK).body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        boolean status = userService.delete(id);
    }
}
