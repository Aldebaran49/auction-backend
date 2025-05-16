package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreev.auction.dto.*;
import ru.andreev.auction.mapper.BidCreateEditMapper;
import ru.andreev.auction.mapper.BidReadMapper;
import ru.andreev.auction.repository.BidRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final BidReadMapper bidReadMapper;
    private final BidCreateEditMapper bidCreateEditMapper;
    private final LotService lotService;

    public List<BidReadDto> findAll() {
        return bidRepository.findAll().stream().map(bidReadMapper::map).toList();
    }
    public Optional<BidReadDto> findById(Long id) {
        return bidRepository.findById(id).map(bidReadMapper::map);
    }
    @Transactional
    public BidCreateResponse create (BidCreateEditDto dto) {
        boolean isAmountCorrect = checkBidForCorrectAmount(dto);
        boolean isTimeCorrect = checkBidForCorrectTime(dto);
        if (isAmountCorrect && isTimeCorrect) {
            lotService.updateAmount(dto.getLotId(), dto.getAmount());
            return new BidCreateResponse(
                    Optional.of(dto)
                            .map(bidCreateEditMapper::map)
                            .map(bidRepository::save)
                            .map(bidReadMapper::map)
                            .orElse(null),
                    true,
                    "Bid created successfully"
            );
        }
        else if (!isAmountCorrect) {
            return new BidCreateResponse(
                    null,
                    false,
                    "Bid amount should be greater than lot current price"
            );
        }
        else {
            return new BidCreateResponse(
                    null,
                    false,
                    "Lot expired"
            );
        }
    }
    @Transactional
    public Optional<BidReadDto> update (Long id, BidCreateEditDto dto) {
        return bidRepository.findById(id)
                .map(l -> bidCreateEditMapper.map(dto, l))
                .map(bidRepository::saveAndFlush)
                .map(bidReadMapper::map);
    }
    @Transactional
    public boolean delete (Long id) {
        return bidRepository.findById(id)
                .map(l -> {
                    bidRepository.delete(l);
                    bidRepository.flush();
                    return true;
                }).orElse(false);
    }

    public boolean checkBidForCorrectAmount(BidCreateEditDto dto) {
        return dto.getAmount().compareTo(lotService.findById(dto.getLotId()).get().getPrice()) > 0;
    }

    public boolean checkBidForCorrectTime(BidCreateEditDto dto) {
        return dto.getBidTime().isBefore(lotService.findById(dto.getLotId()).get().getExpiredAt());
    }

}
