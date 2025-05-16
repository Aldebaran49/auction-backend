package ru.andreev.auction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.LotService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
public class LotRestController {
    private final LotService lotService;

    @GetMapping("")
    public ResponseEntity<List<LotReadDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lotService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<LotReadDto>> findAllActive() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lotService.findAllActive());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<LotReadDto>> findAllExpired() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lotService.findAllExpired());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotReadDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lotService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<LotReadDto> create(@Valid LotCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lotService.create(dto));
    }

    @PutMapping("")
    public ResponseEntity<LotReadDto> update(Long id, @Valid LotCreateEditDto dto) {
        Optional<LotReadDto> response = lotService.update(id, dto);
        return response.map(lot -> ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(lot)).orElseGet(() -> ResponseEntity
                .badRequest().body(null));
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean state = lotService.delete(id);
    }
}
