package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreev.auction.dto.BidCreateEditDto;
import ru.andreev.auction.dto.BidReadDto;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.mapper.BidReadMapper;
import ru.andreev.auction.mapper.LotCreateEditMapper;
import ru.andreev.auction.mapper.LotReadMapper;
import ru.andreev.auction.repository.BidRepository;
import ru.andreev.auction.repository.LotRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LotService {
    private final LotRepository lotRepository;
    private final LotCreateEditMapper lotCreateEditMapper;
    private final LotReadMapper lotReadMapper;
    private final BidRepository bidRepository;
    private final BidReadMapper bidReadMapper;

    public List<LotReadDto> findAll() {
        return lotRepository.findAll().stream().map(lotReadMapper::map).toList();
    }
    public LotReadDto findById(Long id) {
        return lotRepository.findById(id).map(lotReadMapper::map).orElse(null);
    }
    @Transactional
    public LotReadDto create (LotCreateEditDto dto) {
        return Optional.of(dto)
                .map(lotCreateEditMapper::map)
                .map(lotRepository::save)
                .map(lotReadMapper::map)
                .orElse(null);
    }
    @Transactional
    public Optional<LotReadDto> update (Long id, LotCreateEditDto dto) {
        return lotRepository.findById(id)
                .map(l -> lotCreateEditMapper.map(dto, l))
                .map(lotRepository::saveAndFlush)
                .map(lotReadMapper::map);
    }
    @Transactional
    public boolean delete (Long id) {
        return lotRepository.findById(id)
                .map(l -> {
                    lotRepository.delete(l);
                    lotRepository.flush();
                    return true;
                }).orElse(false);
    }

    public void updateAmount(Long lotId, BigDecimal amount) {
        LotReadDto oldLotDto = findById(lotId);
        LotCreateEditDto newLotDto = new LotCreateEditDto(
                oldLotDto.getTitle(),
                oldLotDto.getDescription(),
                amount,
                oldLotDto.getCreatedAt(),
                oldLotDto.getExpiredAt()
        );
        update(lotId, newLotDto);
    }

    public Optional<Long> getWinnerId(Long lotId) {
        return bidRepository.findTopByLotIdOrderByAmountDesc(lotId)
                .map(bid -> bid.getUser().getId());
    }

    public List<LotReadDto> findAllActive() {
        return lotRepository.findByExpiredAtAfter(LocalDateTime.now())
                .stream()
                .map(lotReadMapper::map)
                .toList();
    }

    public List<LotReadDto> findAllExpired() {
        return lotRepository.findByExpiredAtBefore(LocalDateTime.now())
                .stream()
                .map(lotReadMapper::map)
                .toList();
    }
}
