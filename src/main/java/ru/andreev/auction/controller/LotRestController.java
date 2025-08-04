package ru.andreev.auction.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.andreev.auction.dto.*;
import ru.andreev.auction.entity.UserDetailsImpl;
import ru.andreev.auction.service.BidService;
import ru.andreev.auction.service.LotService;

import java.util.List;

@RestController
@RequestMapping("/api/lots")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class LotRestController {
    private final LotService lotService;
    private final BidService bidService;

    @GetMapping("")
    public ResponseEntity<List<LotReadDto>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(lotService.findAll());
    }

    //ПЕРЕДЕЛАТЬ. СВЕРКА ДОЛЖНА БЫТЬ ПО СТАТУСУ - active / expired. СЕРВЕР НЕ ДОЛЖЕН ДОВЕРЯТЬ ВРЕМЕНИ КЛИЕНТА
    /*@GetMapping("/active")
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
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<LotReadDto> findById(@PathVariable("id") Long id) {
        return lotService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<LotReadDto> createLot(@Valid @RequestBody LotCreateEditDto dto,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(lotService.create(dto, userDetails.getUser().getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotReadDto> update(@PathVariable("id") Long id,
                                             @Valid @RequestBody LotCreateEditDto dto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return lotService.update(id, dto, userDetails.getUser().getId())
                .map(lot -> ResponseEntity.status(HttpStatus.OK).body(lot))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean state = lotService.delete(id, userDetails.getUser().getId());
    }



    //--------- Bids ----------



    @GetMapping("/{id}/bids")
    public ResponseEntity<List<BidReadDto>> findAllBids(@PathVariable("id") Long lotId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bidService.findAllBidsByLotId(lotId));
    }

    @PostMapping("/{id}/bids")
    public ResponseEntity<BidReadDto> createBid (@PathVariable("id") Long lotId,
                                                 @Valid @RequestBody BidCreateRequestDto dto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bidService
                        .create(
                                new BidCreateTransferDto(dto.getAmount(), lotId),
                                userDetails.getUser().getId()
                        ));
    }
}
