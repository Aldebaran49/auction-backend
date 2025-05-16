package ru.andreev.auction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.BidCreateEditDto;
import ru.andreev.auction.dto.BidCreateResponse;
import ru.andreev.auction.dto.BidReadDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.service.BidService;

import java.util.List;
import java.util.Optional;

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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bidService.findById(id));
    }

    @PostMapping("")
    public ResponseEntity<BidCreateResponse> create(BidCreateEditDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bidService.create(dto));
    }

    @PutMapping("")
    public ResponseEntity<BidReadDto> update(Long id, BidCreateEditDto dto) {
        Optional<BidReadDto> response = bidService.update(id, dto);
        return response.map(bid -> ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bid)).orElseGet(() -> ResponseEntity
                .badRequest().body(null));
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean state = bidService.delete(id);
    }

}
