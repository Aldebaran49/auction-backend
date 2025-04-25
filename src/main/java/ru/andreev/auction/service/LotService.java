package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.mapper.LotCreateEditMapper;
import ru.andreev.auction.mapper.LotReadMapper;
import ru.andreev.auction.repository.LotRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LotService {
    private final LotRepository lotRepository;
    private final LotCreateEditMapper lotCreateEditMapper;
    private final LotReadMapper lotReadMapper;

    public List<LotReadDto> findAll() {
        return lotRepository.findAll().stream().map(lotReadMapper::map).toList();
    }
    public LotReadDto findById(Long id) {
        return lotRepository.findById(id).map(lotReadMapper::map).orElse(null);
    }
    public LotReadDto create (LotCreateEditDto dto) {
        return Optional.of(dto)
                .map(lotCreateEditMapper::map)
                .map(lotRepository::save)
                .map(lotReadMapper::map)
                .orElse(null);
    }
    public Optional<LotReadDto> update (Long id, LotCreateEditDto dto) {
        return lotRepository.findById(id)
                .map(l -> lotCreateEditMapper.map(dto, l))
                .map(lotRepository::saveAndFlush)
                .map(lotReadMapper::map);
    }
    public boolean delete (Long id) {
        return lotRepository.findById(id)
                .map(l -> {
                    lotRepository.delete(l);
                    lotRepository.flush();
                    return true;
                }).orElse(false);
    }
}
