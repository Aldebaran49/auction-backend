package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andreev.auction.dto.BidCreateEditDto;
import ru.andreev.auction.dto.BidReadDto;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
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

    public List<BidReadDto> findAll() {
        return bidRepository.findAll().stream().map(bidReadMapper::map).toList();
    }
    public BidReadDto findById(Long id) {
        return bidRepository.findById(id).map(bidReadMapper::map).orElse(null);
    }
    @Transactional
    public BidReadDto create (BidCreateEditDto dto) {
        return Optional.of(dto)
                .map(bidCreateEditMapper::map)
                .map(bidRepository::save)
                .map(bidReadMapper::map)
                .orElse(null);
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

}
