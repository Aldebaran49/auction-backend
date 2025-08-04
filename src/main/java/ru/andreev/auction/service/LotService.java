package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.LotStatus;
import ru.andreev.auction.entity.User;
import ru.andreev.auction.mapper.LotCreateEditMapper;
import ru.andreev.auction.mapper.LotReadMapper;
import ru.andreev.auction.repository.LotRepository;
import ru.andreev.auction.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LotService {
    private final LotRepository lotRepository;
    private final LotCreateEditMapper lotCreateEditMapper;
    private final LotReadMapper lotReadMapper;
    private final UserRepository userRepository;

    public List<LotReadDto> findAll() {
        return lotRepository.findAll().stream().map(lotReadMapper::map).toList();
    }
    public Optional<LotReadDto> findById(Long id) {
        return lotRepository.findById(id).map(lotReadMapper::map);
    }
    @Transactional
    public LotReadDto create (LotCreateEditDto dto, Long ownerId) {
        checkLotCreateEditDtoValid(dto);
        Lot lot = lotCreateEditMapper.map(dto);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot owner not found"));
        lot.setOwner(owner);
        owner.getOwnedLots().add(lot);

        Lot savedLot = lotRepository.save(lot);
        log.info("User {} (id {}) created lot {} (id {})",
                owner.getUsername(), owner.getId(),
                lot.getTitle(), lot.getId());
        return lotReadMapper.map(savedLot);
    }
    @Transactional
    public Optional<LotReadDto> update (Long id, LotCreateEditDto dto, Long userId) {
        checkLotCreateEditDtoValid(dto);
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot owner not found"));
        checkLotAuthority(user, lot);
        lotCreateEditMapper.map(dto, lot);
        lotRepository.saveAndFlush(lot);
        log.info("User {} (id {}) edited lot {} (id {})",
                user.getUsername(), user.getId(),
                lot.getTitle(), lot.getId());
        return Optional.of(lotReadMapper.map(lot));
    }
    @Transactional
    public boolean delete (Long id, Long userId) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot owner not found"));
        checkLotAuthority(user, lot);
        lotRepository.delete(lot);
        lotRepository.flush();
        log.info("Lot {} (id = {}) was deleted", lot.getTitle(), lot.getId());
        return true;
    }

    private void checkLotAuthority(User user, Lot lot) {
        if (!lot.getOwner().getId().equals(user.getId()) || lot.getStartedAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No authority to edit this lot");
        }
    }

    private void checkLotCreateEditDtoValid(LotCreateEditDto dto) {
        if (dto.getExpiredAt().isBefore(dto.getStartedAt()) ||
        dto.getStartedAt().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lot is not valid");
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void processLots() {
        List<Lot> waitingLots = lotRepository.findAllByStatusAndStartedAtBefore(
                LotStatus.WAITING,
                LocalDateTime.now()
        );

        if (!waitingLots.isEmpty()) {
            for (Lot lot : waitingLots) {
                lot.setStatus(LotStatus.ACTIVE);
                log.info("Lot {} (id = {}) is now at sale", lot.getTitle(), lot.getId());
            }
        }

        lotRepository.saveAll(waitingLots);

        List<Lot> expiredLots = lotRepository.findAllByStatusAndExpiredAtBefore(
                LotStatus.ACTIVE,
                LocalDateTime.now()
        );

        if (expiredLots.isEmpty()) {
            log.info("No expired lots found");
            return;
        }

        for (Lot lot: expiredLots) {
            if (lot.getWinner() != null) {
                lot.setStatus(LotStatus.SOLD);
                //notify

            } else {
                lot.setStatus(LotStatus.NOT_SOLD);

                //notify
            }
            log.info("Lot {} (id = {}) was processed. New status: {}",
                    lot.getTitle(), lot.getId(), lot.getStatus());
        }

        lotRepository.saveAll(expiredLots);
    }

    //ПЕРЕДЕЛАТЬ.
    /*public List<LotReadDto> findAllActive() {
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
    }*/
}
