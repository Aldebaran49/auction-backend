package ru.andreev.auction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.service.LotService;

import java.util.List;

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
        return lotService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<LotReadDto> create(@Valid @RequestBody LotCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lotService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotReadDto> update(@PathVariable("id") Long id,
                                             @Valid @RequestBody LotCreateEditDto dto) {
        return lotService.update(id, dto)
                .map(lot -> ResponseEntity.status(HttpStatus.OK).body(lot))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        boolean state = lotService.delete(id);
    }
}
