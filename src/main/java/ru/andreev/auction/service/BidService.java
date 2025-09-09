package ru.andreev.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.andreev.auction.dto.*;
import ru.andreev.auction.entity.Bid;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.LotStatus;
import ru.andreev.auction.entity.User;
import ru.andreev.auction.exception.AuctionInvalidBidException;
import ru.andreev.auction.mapper.BidCreateTransferMapper;
import ru.andreev.auction.mapper.BidReadMapper;
import ru.andreev.auction.repository.BidRepository;
import ru.andreev.auction.repository.LotRepository;
import ru.andreev.auction.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BidService {
    private final BidRepository bidRepository;
    private final BidReadMapper bidReadMapper;
    private final BidCreateTransferMapper bidCreateEditMapper;
    private final LotRepository lotRepository;
    private final UserRepository userRepository;

    public List<BidReadDto> findAllBidsByLotId(Long lotId) {
        return bidRepository.findAllByLot_Id(lotId).stream().map(bidReadMapper::map).toList();
    }

    @Transactional
    public BidReadDto create (BidCreateTransferDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bidder not found"));
        Lot lot = lotRepository.findById(dto.getLotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lot not found"));
        if (lot.getStatus() == LotStatus.WAITING) {
            throw new AuctionInvalidBidException("Lot is not active. Wait before trade starts");
        }
        else if (lot.getStatus() == LotStatus.SOLD
                || lot.getStatus() == LotStatus.NOT_SOLD
                || lot.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AuctionInvalidBidException("Lot expired");
        }
        if (lot.getOwner().getId().equals(userId)) {
            throw new AuctionInvalidBidException("Owner can't bid on it's own lot");
        }

        if (dto.getAmount().compareTo(lot.getBlitzPrice()) >= 0) {
            lot.setCurrentPrice(lot.getBlitzPrice());
            lot.setWinner(user);
            user.getWonLots().add(lot);
            lot.setStatus(LotStatus.SOLD);
            lot.setExpiredAt(LocalDateTime.now());

            Bid finalBid = bidCreateEditMapper.map(dto);
            finalBid.setAmount(lot.getBlitzPrice());
            finalBid = bidRepository.save(finalBid);
            lotRepository.save(lot);


            //notify
            log.info("Lot {} (id = {}) was sold by blitz price by bidder {} (id = {})",
                    lot.getTitle(), lot.getId(), user.getUsername(), user.getId());
            return bidReadMapper.map(finalBid);
        }

        BigDecimal minAllowedBid = lot.getCurrentPrice().add(lot.getBidStep());
        if (dto.getAmount().compareTo(minAllowedBid) < 0)
            throw new AuctionInvalidBidException("Minimal bid is " + minAllowedBid);

        if (lot.getWinner() != null && lot.getWinner().getId().equals(userId))
            throw new AuctionInvalidBidException("You are currently winning this lot");

        lot.setCurrentPrice(dto.getAmount());
        lot.setWinner(user);

        //Anti-sniping (future)

        Bid bid = bidCreateEditMapper.map(dto);
        bid.setOwner(user);
        bid = bidRepository.save(bid);
        lotRepository.save(lot);
        log.info("Bid was registered! User {} (id = {}), lot {} (id = {}), amount: {}",
                user.getUsername(), user.getId(), lot.getTitle(), lot.getId(), bid.getAmount());
        return bidReadMapper.map(bid);
    }
    //Only for admins in future
    @Transactional
    public boolean delete (Long id, Long userId) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bid not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        bidRepository.delete(bid);
        bidRepository.flush();
        //bidService.deletionUpdate();
        return true;
    }


}
