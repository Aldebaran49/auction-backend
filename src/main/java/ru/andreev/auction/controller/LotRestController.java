package ru.andreev.auction.controller;

import lombok.RequiredArgsConstructor;
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
    public List<LotReadDto> findAll() {
        return lotService.findAll();
    }

    @GetMapping("/active")
    public List<LotReadDto> findAllActive() {
        return lotService.findAllActive();
    }

    @GetMapping("/expired")
    public List<LotReadDto> findAllExpired() {
        return lotService.findAllExpired();
    }

    @GetMapping("/{id}")
    public LotReadDto findById(@PathVariable("id") Long id) {
        return lotService.findById(id);
    }

    @PostMapping("")
    public LotReadDto create(LotCreateEditDto lotCreateEditDto) {
        return lotService.create(lotCreateEditDto);
    }

    @PutMapping("")
    public LotReadDto update(Long id, LotCreateEditDto lotCreateEditDto) {
        return lotService.update(id, lotCreateEditDto).orElseThrow();
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean state = lotService.delete(id);
    }
}
