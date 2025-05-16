package ru.andreev.auction.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.BidCreateEditDto;
import ru.andreev.auction.dto.BidCreateResponse;
import ru.andreev.auction.dto.BidReadDto;
import ru.andreev.auction.service.BidService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
public class BidRestController {
    private final BidService bidService;

    @GetMapping("")
    public ResponseEntity<List<BidReadDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bidService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidReadDto> findById(@PathVariable("id") Long id) {
        return bidService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<BidCreateResponse> create(@Valid @RequestBody BidCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bidService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BidReadDto> update(@PathVariable("id") Long id,
                                             @Valid @RequestBody BidCreateEditDto dto) {
        return bidService.update(id, dto)
                .map(bid -> ResponseEntity.status(HttpStatus.OK).body(bid))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        boolean state = bidService.delete(id);
    }

}
