package ru.andreev.auction.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.andreev.auction.dto.UserCreateEditDto;
import ru.andreev.auction.dto.UserReadDto;
import ru.andreev.auction.entity.Role;
import ru.andreev.auction.entity.User;
import ru.andreev.auction.mapper.UserCreateEditMapper;
import ru.andreev.auction.mapper.UserReadMapper;
import ru.andreev.auction.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserReadMapper userReadMapper;
    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void create_shouldCreateUserSuccessfully () {
        UserCreateEditDto inputDto = new UserCreateEditDto(
                "testuser",
                "123456",
                "test@example.com",
                Role.USER);
        User userToSave = new User();
        userToSave.setUsername("testuser");
        userToSave.setEmail("test@example.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");

        UserReadDto expectedResult = new UserReadDto(1L,
                "testuser",
                "123456",
                "test@example.com",
                Role.USER,
                Collections.emptyList());

        when(userCreateEditMapper.map(inputDto)).thenReturn(userToSave);

        when(userRepository.save(userToSave)).thenReturn(savedUser);

        when(userReadMapper.map(savedUser)).thenReturn(expectedResult);



        UserReadDto actualResult = userService.create(inputDto);




        assertNotNull(actualResult);
        assertEquals(expectedResult.getId(), actualResult.getId());
        assertEquals(expectedResult.getUsername(), actualResult.getUsername());


        verify(userCreateEditMapper).map(inputDto);
        verify(userRepository).save(userToSave);
        verify(userReadMapper).map(savedUser);
    }

    @Test
    void findById_shouldFindUser() {
        Long inputId = 1L;
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("123456");
        savedUser.setEmail("test@example.com");
        savedUser.setRole(Role.USER);
        savedUser.setBids(Collections.emptyList());

        UserReadDto expectedResult = new UserReadDto(1L,
                "testuser",
                "123456",
                "test@example.com",
                Role.USER,
                Collections.emptyList());

        when(userRepository.findById(inputId)).thenReturn(Optional.of(savedUser));
        when(userReadMapper.map(savedUser)).thenReturn(expectedResult);

        Optional<UserReadDto> actualResult = userService.findById(inputId);

        assertTrue(actualResult.isPresent());
        assertEquals(actualResult.get(), expectedResult);

        verify(userRepository).findById(inputId);
        verify(userReadMapper).map(savedUser);
    }

    @Test
    void findById_shouldNotFindUser() {
        Long inputId = 1L;
        when(userRepository.findById(inputId)).thenReturn(Optional.empty());

        Optional<UserReadDto> expectedResult = Optional.empty();
        Optional<UserReadDto> actualResult = userService.findById(inputId);

        assertEquals(actualResult, expectedResult);
        verify(userReadMapper, never()).map(any(User.class));
    }

    @Test
    void update_successful() {
        Long inputId = 1L;
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("123456");
        savedUser.setEmail("test@example.com");
        savedUser.setRole(Role.USER);
        savedUser.setBids(Collections.emptyList());

        UserCreateEditDto dto = new UserCreateEditDto(
                "testuser2",
                "1234674",
                "test@example2.com",
                Role.ADMIN
        );

        User editedUser = new User();
        editedUser.setId(1L);
        editedUser.setUsername("testuser2");
        editedUser.setPassword("1234674");
        editedUser.setEmail("test@example2.com");
        editedUser.setRole(Role.ADMIN);
        editedUser.setBids(Collections.emptyList());

        UserReadDto expectedResult = new UserReadDto(
                1L,
                "testuser2",
                "1234674",
                "test@example2.com",
                Role.ADMIN,
                Collections.emptyList()
        );

        when(userRepository.findById(inputId)).thenReturn(Optional.of(savedUser));
        when(userCreateEditMapper.map(dto, savedUser)).thenReturn(editedUser);
        when(userRepository.saveAndFlush(editedUser)).thenReturn(editedUser);
        when(userReadMapper.map(editedUser)).thenReturn(expectedResult);

        Optional<UserReadDto> actualResult = userService.update(inputId, dto);

        assertTrue(actualResult.isPresent());
        assertEquals(actualResult.get(), expectedResult);

        verify(userRepository).findById(inputId);
        verify(userCreateEditMapper).map(dto, savedUser);
        verify(userRepository).saveAndFlush(editedUser);
        verify(userReadMapper).map(editedUser);
    }

    @Test
    void update_userNotFound() {
        Long inputId = 1L;
        UserCreateEditDto dto = new UserCreateEditDto(
                "testuser2",
                "1234674",
                "test@example2.com",
                Role.ADMIN
        );

        when(userRepository.findById(inputId)).thenReturn(Optional.empty());

        Optional<UserReadDto> expectedResult = Optional.empty();
        Optional<UserReadDto> actualResult = userService.update(inputId, dto);

        assertEquals(expectedResult, actualResult);

        verify(userReadMapper, never()).map(any(User.class));
        verify(userCreateEditMapper, never()).map(any(UserCreateEditDto.class), any(User.class));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }
}
