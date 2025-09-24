package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.Entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    // fake eyyyy
    @Mock
    private UserRepository userRepository;
    // UserRepository(fake) -> UserService(real)
    @InjectMocks
    private UserService userService;

    @Test
    public void name() {
        // arrange
        // act
        // assert
    }

    @Test
    public void createUser_ShouldReturnUser_WhenEmailIsValid() {
        // arrange
        User inputUser = new User(null, "John Doe", "john.doe@example.com");
        User outputUser = new User(1L, "John Doe", "john.doe@example.com");
        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(false);
        when(this.userRepository.save(any())).thenReturn(outputUser);
        // act
        User result = this.userService.createUser(inputUser);
        // assert
        assertEquals(1L, result.getId());
    }

    @Test
    public void createUser_ShouldReturnUser_WhenEmailIsInValid() {
        // arrange
        User inputUser = new User(null, "John Doe", "john.doe@example.com");

        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(true);
        // act
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            this.userService.createUser(inputUser);
        });
        // assert
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() {
        // arrange
        List<User> outputUsers = new ArrayList<>();
        outputUsers.add(new User(1L, "John Doe", "john.doe@example.com"));
        outputUsers.add(new User(2L, "Jane Marry", "jane.marrey@example.com"));
        // act
        when(this.userRepository.findAll()).thenReturn(outputUsers);
        List<User> result = this.userService.getAllUsers();
        // assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Marry", result.get(1).getName());
    }

    @Test
    public void getUserById_ShouldReturnOptionalUser() {
        // arrange
        Long userId = 1L;
        Optional<User> outputUser = Optional.of(new User(1L, "John Doe", "john.doe@example.com"));
        when(this.userRepository.findById(userId)).thenReturn(outputUser);
        // act
        Optional<User> result = this.userService.getUserById(userId);
        // assert
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    public void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // arrange
        Long inputUserId = 1L;
        when(this.userRepository.existsById(inputUserId)).thenReturn(false);
        // act
        Exception ex = assertThrows(NoSuchElementException.class, () -> {
            this.userService.deleteUser(inputUserId);
        });
        // assert
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    public void deleteUser_ShouldReturnVoid_WhenUserFound() {
        // arrange
        Long inputUserId = 1L;
        when(this.userRepository.existsById(inputUserId)).thenReturn(true);
        // act
        this.userService.deleteUser(inputUserId);
        // assert
        verify(this.userRepository).deleteById(inputUserId);
    }

    @Test
    public void updateUser_ShouldThrowException_WhenUserNotFound() {
    }

    @Test
    public void updateUser_ShouldReturnUpdatedUser_WhenUserFound() {
        // arrange
        Long inputId = 1L;
        User inputUser = new User(1L, "John Doe Updated", "emailUpdated");
        when(this.userRepository.findById(inputId)).thenReturn(Optional.of(inputUser));
        when(this.userRepository.save(any())).thenReturn(inputUser);
        // act
        User result = this.userService.updateUser(inputId, inputUser);
        // assert
        assertEquals("John Doe Updated", result.getName());
    }

}

// public User createUser(User user) {
// if (userRepository.existsByEmail(user.getEmail())) {
// throw new IllegalArgumentException("Email already exists");
// }
// return userRepository.save(user);
// }
