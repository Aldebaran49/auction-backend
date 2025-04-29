package ru.andreev.auction.controller;

import lombok.RequiredArgsConstructor;
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
    public List<BidReadDto> findAll() {
        return bidService.findAll();
    }

    @GetMapping("/{id}")
    public BidReadDto findById(@PathVariable("id") Long id) {
        return bidService.findById(id);
    }

    @PostMapping("")
    public BidCreateResponse create(BidCreateEditDto bidCreateEditDto) {
        return bidService.create(bidCreateEditDto);
    }

    @PutMapping("")
    public BidReadDto update(Long id, BidCreateEditDto bidCreateEditDto) {
        return bidService.update(id, bidCreateEditDto).orElseThrow();
    }

    @DeleteMapping("")
    public void delete(Long id) {
        boolean state = bidService.delete(id);
    }

}
