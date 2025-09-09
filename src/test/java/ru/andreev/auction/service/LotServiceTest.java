package ru.andreev.auction.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.andreev.auction.dto.LotCreateEditDto;
import ru.andreev.auction.dto.LotReadDto;
import ru.andreev.auction.entity.Lot;
import ru.andreev.auction.entity.LotStatus;
import ru.andreev.auction.entity.Role;
import ru.andreev.auction.entity.User;
import ru.andreev.auction.mapper.LotCreateEditMapper;
import ru.andreev.auction.mapper.LotReadMapper;
import ru.andreev.auction.repository.LotRepository;
import ru.andreev.auction.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LotServiceTest {
    @Mock
    private LotRepository lotRepository;
    @Mock
    private LotCreateEditMapper lotCreateEditMapper;
    @Mock
    private LotReadMapper lotReadMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private LotService lotService;

    LotCreateEditDto getInputDto(LocalDateTime timeNow, Integer startedDays, Integer expiredDays) {
        return new LotCreateEditDto(
                "test",
                "testDescription",
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(100.0),
                timeNow.plusDays(startedDays),
                timeNow.plusDays(expiredDays)
        );
    }

    @Test
    void findById_shouldFindLot() {
        Long lotId = 1L;
        Lot savedLot = new Lot(
                1L,
                "test",
                "testDescription",
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(100.0),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                null,
                null
        );
        LotReadDto expectedResult = new LotReadDto(
                1L,
                "test",
                "testDescription",
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(100.0),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                LotStatus.ACTIVE,
                Collections.emptyList(),
                null,
                null
        );

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));
        when(lotReadMapper.map(savedLot)).thenReturn(expectedResult);

        Optional<LotReadDto> actualResult = lotService.findById(lotId);

        assertTrue(actualResult.isPresent());
        assertEquals(actualResult, Optional.of(expectedResult));

        verify(lotRepository).findById(lotId);
        verify(lotReadMapper).map(savedLot);
    }

    @Test
    void findById_shouldNotFindLot() {
        Long lotId = 1L;

        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        Optional<LotReadDto> expectedResult = Optional.empty();
        Optional<LotReadDto> actualResult = lotService.findById(lotId);

        assertEquals(expectedResult, actualResult);

        verify(lotReadMapper, never()).map(any(Lot.class));
    }

    @Test
    void create_successful() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);
        User owner = new User();
        owner.setId(userId);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        Lot lotToSave = new Lot();
        lotToSave.setTitle("test");
        lotToSave.setDescription("testDescription");
        lotToSave.setStartPrice(BigDecimal.valueOf(10.0));
        lotToSave.setBidStep(BigDecimal.valueOf(5.0));
        lotToSave.setBlitzPrice(BigDecimal.valueOf(100.0));
        lotToSave.setCurrentPrice(BigDecimal.valueOf(10.0));
        lotToSave.setStatus(LotStatus.WAITING);
        lotToSave.setCreatedAt(timeNow);
        lotToSave.setStartedAt(timeNow.plusDays(1));
        lotToSave.setExpiredAt(timeNow.plusDays(2));

        Lot savedLot = new Lot();
        savedLot.setId(1L);
        savedLot.setTitle("test");
        savedLot.setDescription("testDescription");
        savedLot.setStartPrice(BigDecimal.valueOf(10.0));
        savedLot.setBidStep(BigDecimal.valueOf(5.0));
        savedLot.setBlitzPrice(BigDecimal.valueOf(100.0));
        savedLot.setCurrentPrice(BigDecimal.valueOf(10.0));
        savedLot.setStatus(LotStatus.WAITING);
        savedLot.setCreatedAt(timeNow);
        savedLot.setStartedAt(timeNow.plusDays(1));
        savedLot.setExpiredAt(timeNow.plusDays(2));
        savedLot.setOwner(owner);

        LotReadDto expectedResult = new LotReadDto(
                1L,
                "test",
                "testDescription",
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(100.0),
                timeNow,
                timeNow.plusDays(1),
                timeNow.plusDays(2),
                LotStatus.WAITING,
                Collections.emptyList(),
                3L,
                null
                );
        when(lotCreateEditMapper.map(inputDto)).thenReturn(lotToSave);
        when(lotRepository.save(lotToSave)).thenReturn(savedLot);
        when(lotReadMapper.map(savedLot)).thenReturn(expectedResult);
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));

        LotReadDto actualResult = lotService.create(inputDto, userId);

        assertEquals(actualResult, expectedResult);
        assertFalse(owner.getOwnedLots().isEmpty());
        assertEquals(owner.getOwnedLots().get(0), lotToSave);

        verify(lotCreateEditMapper).map(inputDto);
        verify(lotRepository).save(lotToSave);
        verify(lotReadMapper).map(savedLot);
        verify(userRepository).findById(userId);
    }

    @Test
    void create_userNotFound() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.create(inputDto, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot owner not found"));
    }

    @Test
    void create_lotExpiredBeforeStarted() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        LotCreateEditDto inputDto = getInputDto(timeNow, 2, 1);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.create(inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }

    @Test
    void create_lotExpiredBeforeNow() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        LotCreateEditDto inputDto = getInputDto(timeNow, -2, 1);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.create(inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }

    @Test
    void create_lotStartedBeforeNow() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        LotCreateEditDto inputDto = getInputDto(timeNow, 2, -1);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.create(inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }



    @Test
    void update_successful() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);

        User owner = new User();
        owner.setId(userId);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                owner,
                null
        );

        Lot editedLot = new Lot();
        editedLot.setId(1L);
        editedLot.setTitle("test");
        editedLot.setDescription("testDescription");
        editedLot.setStartPrice(BigDecimal.valueOf(10.0));
        editedLot.setBidStep(BigDecimal.valueOf(5.0));
        editedLot.setBlitzPrice(BigDecimal.valueOf(100.0));
        editedLot.setCurrentPrice(BigDecimal.valueOf(10.0));
        editedLot.setStatus(LotStatus.WAITING);
        editedLot.setCreatedAt(timeNow);
        editedLot.setStartedAt(timeNow.plusDays(1));
        editedLot.setExpiredAt(timeNow.plusDays(2));
        editedLot.setOwner(owner);

        LotReadDto expectedResult = new LotReadDto(
                1L,
                "test",
                "testDescription",
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(100.0),
                timeNow,
                timeNow.plusDays(1),
                timeNow.plusDays(2),
                LotStatus.WAITING,
                Collections.emptyList(),
                3L,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));
        when(lotCreateEditMapper.map(inputDto, savedLot)).thenReturn(editedLot);
        when(lotRepository.saveAndFlush(editedLot)).thenReturn(editedLot);
        when(lotReadMapper.map(editedLot)).thenReturn(expectedResult);

        Optional<LotReadDto> actualResult = lotService.update(lotId, inputDto, userId);

        assertTrue(actualResult.isPresent());
        assertEquals(actualResult.get(), expectedResult);

        verify(userRepository).findById(userId);
        verify(lotRepository).findById(lotId);
        verify(lotCreateEditMapper).map(inputDto, savedLot);
        verify(lotRepository).saveAndFlush(editedLot);
        verify(lotReadMapper).map(editedLot);
    }

    @Test
    void update_lotNotFound() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);

        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot not found"));
    }

    @Test
    void update_userNotFound() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);
        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                null,
                null
        );

        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot owner not found"));
    }

    @Test
    void update_notOwner() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);

        User owner = new User();
        owner.setId(5L);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        User user = new User();
        user.setId(userId);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                owner,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No authority to edit this lot"));
    }

    @Test
    void update_lotStarted() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, 2);

        User owner = new User();
        owner.setId(userId);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                owner,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No authority to edit this lot"));
    }

    @Test
    void update_startedBeforeNow() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, -1, 2);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }

    @Test
    void update_expiredBeforeNow() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 1, -1);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }

    @Test
    void update_expiredBeforeStarted() {
        LocalDateTime timeNow = LocalDateTime.now();
        Long userId = 3L;
        Long lotId = 1L;

        LotCreateEditDto inputDto = getInputDto(timeNow, 2, 1);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.update(lotId, inputDto, userId));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot is not valid"));
    }



    @Test
    void delete_successful() {
        Long userId = 3L;
        Long lotId = 1L;

        User user = new User();
        user.setId(3L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                user,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        assertTrue(lotService.delete(lotId, userId));
    }

    @Test
    void delete_userNotFound() {
        Long userId = 3L;
        Long lotId = 1L;

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                null,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.delete(lotId, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot owner not found"));
    }

    @Test
    void delete_lotNotFound() {
        Long userId = 3L;
        Long lotId = 1L;

        when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.delete(lotId, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Lot not found"));
    }

    @Test
    void delete_notOwner() {
        Long userId = 3L;
        Long lotId = 1L;

        User owner = new User();
        owner.setId(5L);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        User user = new User();
        user.setId(userId);
        owner.setUsername("testuser");
        owner.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().plusDays(20),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                owner,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.delete(lotId, userId));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No authority to edit this lot"));
    }

    @Test
    void delete_lotStarted() {
        Long userId = 3L;
        Long lotId = 1L;

        User user = new User();
        user.setId(3L);
        user.setUsername("testuser");
        user.setRole(Role.USER);

        Lot savedLot = new Lot(
                1L,
                "test2",
                "testDescription2",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(500.0),
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1000.0),
                LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10),
                Collections.emptyList(),
                LotStatus.ACTIVE,
                user,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lotRepository.findById(lotId)).thenReturn(Optional.of(savedLot));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> lotService.delete(lotId, userId));
        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertTrue(ex.getReason().contains("No authority to edit this lot"));
    }
}
