package ru.ilyin.userservice.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ilyin.userservice.dto.UserRequestDto;
import ru.ilyin.userservice.dto.UserResponseDto;
import ru.ilyin.userservice.exception.ResourceNotFoundException;
import ru.ilyin.userservice.mapper.UserMapper;
import ru.ilyin.userservice.model.User;
import ru.ilyin.userservice.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldReturnUserResponseDto_WhenValidInput() {

        UserRequestDto requestDto = new UserRequestDto("New User", "new@mail.ru", 25);

        User user = User.builder().name("New User").email("new@mail.ru").age(25).build();
        User savedUser = User.builder().id(1L).name("New User").email("new@mail.ru").age(25).build();
        UserResponseDto responseDto = new UserResponseDto(1L, "New User", "new@mail.ru", 25, LocalDateTime.now());

        when(userRepository.existsByEmail("new@mail.ru")).thenReturn(false);
        when(userMapper.toEntity(requestDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New User", result.getName());
        verify(userRepository, times(1)).save(user);

    }

    void getUserById_ShouldReturnUserResponseDto_WhenUserExists() {
        Long userId = 1L;
        User user = User.builder().id(userId).name("Test User").email("test@mail.ru").age(30).build();
        UserResponseDto responseDto = new UserResponseDto(userId, "Test User", "test@mail.ru", 30, LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
    }

    @Test
    void getUserById_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserResponseDto_WhenValidInput() {

        Long userId = 1L;
        UserRequestDto requestDto = new UserRequestDto("Updated User", "updated@mail.ru", 35);
        User existingUser = User.builder().id(userId).name("Test User").email("test@mail.ru").age(30).build();
        User updatedUser = User.builder().id(userId).name("Updated User").email("updated@mail.ru").age(35).build();
        UserResponseDto responseDto = new UserResponseDto(userId, "Updated User", "updated@mail.ru", 35, LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("updated@mail.ru")).thenReturn(false);
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(userId, requestDto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Updated User", result.getName());
        verify(userMapper).updateEntity(requestDto, existingUser);

    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
