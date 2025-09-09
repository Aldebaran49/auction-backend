package ru.andreev.auction.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ru.andreev.auction.controller.LotRestController;
import ru.andreev.auction.dto.BidCreateTransferDto;
import ru.andreev.auction.dto.BidReadDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository bidRepository;
    @Mock
    private LotRepository lotRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BidReadMapper bidReadMapper;
    @Mock
    private BidCreateTransferMapper bidCreateEditMapper;

    @InjectMocks
    private BidService bidService;

    @Test
    void findAllBidsByLotId_BidsExist() {
        Long lotId = 1L;

        User bidder = User.builder().id(10L).build();
        Lot lot = Lot.builder().id(lotId).build();

        Bid bid1 = Bid.builder()
                .id(101L)
                .amount(new BigDecimal("150.00"))
                .bidTime(LocalDateTime.now())
                .owner(bidder)
                .lot(lot)
                .build();

        Bid bid2 = Bid.builder()
                .id(102L)
                .amount(new BigDecimal("200.00"))
                .bidTime(LocalDateTime.now().plusMinutes(1))
                .owner(bidder)
                .lot(lot)
                .build();

        List<Bid> bidListFromRepo = List.of(bid1, bid2);

        BidReadDto dto1 = new BidReadDto(
                101L,
                BigDecimal.valueOf(150.0),
                bid1.getBidTime(),
                10L,
                1L
        );

        BidReadDto dto2 = new BidReadDto(
                102L,
                BigDecimal.valueOf(200.0),
                bid2.getBidTime(),
                10L,
                1L
        );

        when(bidRepository.findAllByLot_Id(lotId)).thenReturn(bidListFromRepo);
        when(bidReadMapper.map(bid1)).thenReturn(dto1);
        when(bidReadMapper.map(bid2)).thenReturn(dto2);

        List<BidReadDto> actualResult = bidService.findAllBidsByLotId(lotId);

        assertNotNull(actualResult);
        assertEquals(2, actualResult.size());
        assertTrue(actualResult.contains(dto1));
        assertTrue(actualResult.contains(dto2));

        verify(bidRepository, times(1)).findAllByLot_Id(lotId);
        verify(bidReadMapper, times(1)).map(bid1);
        verify(bidReadMapper, times(1)).map(bid2);
    }
    @Test
    void findAllBidsByLotId_NoBidsExist() {
        Long lotId = 2L;

        when(bidRepository.findAllByLot_Id(lotId)).thenReturn(Collections.emptyList());

        List<BidReadDto> actualResult = bidService.findAllBidsByLotId(lotId);

        assertNotNull(actualResult);
        assertTrue(actualResult.isEmpty());

        verify(bidRepository).findAllByLot_Id(lotId);
        verify(bidReadMapper, never()).map(any(Bid.class));
    }

    @Test
    void create_shouldPlaceBidSuccessfully_whenAllConditionsAreMet() {
        Long lotId = 1L;
        Long userId = 2L;
        User owner = User.builder().id(3L).build();
        User bidder = User.builder().id(userId).username("bidder").build();

        Lot lot = Lot.builder()
                .id(lotId)
                .owner(owner)
                .status(LotStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .currentPrice(new BigDecimal("100.00"))
                .bidStep(new BigDecimal("10.00"))
                .blitzPrice(new BigDecimal("1000.00"))
                .build();

        BidCreateTransferDto createDto = new BidCreateTransferDto(new BigDecimal("120.00"), lotId);
        Bid bidToSave = Bid.builder().build();
        Bid savedBid = Bid.builder().id(100L).build();
        BidReadDto expectedDto = new BidReadDto(100L, null, null, null, null);


        when(userRepository.findById(userId)).thenReturn(Optional.of(bidder));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(bidCreateEditMapper.map(createDto)).thenReturn(bidToSave);
        when(bidRepository.save(bidToSave)).thenReturn(savedBid);
        when(bidReadMapper.map(savedBid)).thenReturn(expectedDto);

        BidReadDto actualDto = bidService.create(createDto, userId);

        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);

        ArgumentCaptor<Lot> lotCaptor = ArgumentCaptor.forClass(Lot.class);
        verify(lotRepository).save(lotCaptor.capture());
        Lot savedLot = lotCaptor.getValue();

        assertEquals(new BigDecimal("120.00"), savedLot.getCurrentPrice());
        assertEquals(bidder, savedLot.getWinner());

        assertEquals(bidder, bidToSave.getOwner());
    }

    @Test
    void create_shouldSellLotByBlitzPrice_whenBidAmountIsEqualToBlitz() {
        Long lotId = 1L;
        Long userId = 2L;
        User owner = User.builder().id(3L).build();
        User bidder = User.builder().id(userId).username("bidder").build();

        Lot lot = Lot.builder()
                .id(lotId)
                .owner(owner)
                .status(LotStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .currentPrice(new BigDecimal("100.00"))
                .blitzPrice(new BigDecimal("1000.00"))
                .build();

        BidCreateTransferDto createDto = new BidCreateTransferDto(new BigDecimal("1000.00"), lotId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(bidder));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));
        when(bidCreateEditMapper.map(any())).thenReturn(new Bid());

        bidService.create(createDto, userId);

        ArgumentCaptor<Lot> lotCaptor = ArgumentCaptor.forClass(Lot.class);
        verify(lotRepository).save(lotCaptor.capture());
        Lot savedLot = lotCaptor.getValue();

        assertEquals(LotStatus.SOLD, savedLot.getStatus());
        assertEquals(new BigDecimal("1000.00"), savedLot.getCurrentPrice());
        assertEquals(bidder, savedLot.getWinner());
        assertTrue(savedLot.getExpiredAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        ArgumentCaptor<Bid> bidCaptor = ArgumentCaptor.forClass(Bid.class);
        verify(bidRepository).save(bidCaptor.capture());
        Bid savedBid = bidCaptor.getValue();

        assertEquals(new BigDecimal("1000.00"), savedBid.getAmount());
    }


    @Test
    void create_shouldThrowException_whenLotNotFound() {
        when(lotRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        BidCreateTransferDto dto = new BidCreateTransferDto(BigDecimal.TEN, 1L);

        assertThrows(ResponseStatusException.class, () -> bidService.create(dto, 1L));
    }

    @Test
    void create_shouldThrowException_whenOwnerBidsOnOwnLot() {
        Long lotId = 1L;
        Long userId = 2L;
        User ownerAndBidder = User.builder().id(userId).build();

        Lot lot = Lot.builder()
                .id(lotId)
                .owner(ownerAndBidder)
                .status(LotStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(ownerAndBidder));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        BidCreateTransferDto dto = new BidCreateTransferDto(BigDecimal.TEN, lotId);

        AuctionInvalidBidException exception = assertThrows(
                AuctionInvalidBidException.class,
                () -> bidService.create(dto, userId)
        );
        assertEquals("Owner can't bid on it's own lot", exception.getMessage());
    }

    @Test
    void create_shouldThrowException_whenBidAmountIsTooLow() {
        Long lotId = 1L;
        Long userId = 2L;
        User owner = User.builder().id(3L).build();
        User bidder = User.builder().id(userId).build();

        Lot lot = Lot.builder()
                .id(lotId)
                .owner(owner)
                .status(LotStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusDays(1))
                .currentPrice(new BigDecimal("100.00"))
                .bidStep(new BigDecimal("10.00"))
                .blitzPrice(new BigDecimal("1000.00"))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(bidder));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

        BidCreateTransferDto dto = new BidCreateTransferDto(new BigDecimal("109.99"), lotId);

        AuctionInvalidBidException exception = assertThrows(
                AuctionInvalidBidException.class,
                () -> bidService.create(dto, userId)
        );

        assertTrue(exception.getMessage().startsWith("Minimal bid is "));
    }
}