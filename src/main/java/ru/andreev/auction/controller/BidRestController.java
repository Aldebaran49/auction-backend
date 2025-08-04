package ru.andreev.auction.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@SecurityRequirement(name = "bearerAuth")
public class BidRestController {
    /*
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
    public ResponseEntity<BidCreateResponse> create(@Valid @RequestBody BidCreateEditDto dto,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bidService.create(dto, userDetails.getUser().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BidReadDto> update(@PathVariable("id") Long id,
                                             @Valid @RequestBody BidCreateEditDto dto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return bidService.update(id, dto, userDetails.getUser().getId())
                .map(bid -> ResponseEntity.status(HttpStatus.OK).body(bid))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean state = bidService.delete(id, userDetails.getUser().getId());
    }
    */
}
