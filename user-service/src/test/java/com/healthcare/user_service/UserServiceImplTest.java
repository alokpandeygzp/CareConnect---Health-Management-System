package com.healthcare.user_service;

import com.healthcare.user_service.entities.Role;
import com.healthcare.user_service.entities.User;
import com.healthcare.user_service.exceptions.ApiException;
import com.healthcare.user_service.exceptions.ResourceNotFoundException;
import com.healthcare.user_service.payloads.UserDto;
import com.healthcare.user_service.repositories.RoleRepo;
import com.healthcare.user_service.repositories.UserRepo;
import com.healthcare.user_service.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDto mockUserDto;
    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUserDto = new UserDto();
        mockUserDto.setId(1);
        mockUserDto.setName("John Doe");
        mockUserDto.setEmail("john.doe@example.com");
        mockUserDto.setPassword("password123");

        mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setName("NORMAL_USER");

        mockUser = new User();
        mockUser.setId(1);
        mockUser.setName("John Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("encodedPassword123");
        mockUser.setRole(mockRole);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(modelMapper.map(mockUserDto, User.class)).thenReturn(mockUser);
        when(userRepo.save(mockUser)).thenReturn(mockUser);
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(mockUserDto);

        // Act
        UserDto createdUser = userService.createUser(mockUserDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals(mockUserDto.getName(), createdUser.getName());
        verify(userRepo, times(1)).save(mockUser);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        when(userRepo.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(mockUserDto.getPassword())).thenReturn("encodedPassword123");
        when(userRepo.save(mockUser)).thenReturn(mockUser);
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(mockUserDto);

        // Act
        UserDto updatedUser = userService.updateUser(mockUserDto, mockUser.getId());

        // Assert
        assertNotNull(updatedUser);
        assertEquals(mockUserDto.getName(), updatedUser.getName());
        verify(userRepo, times(1)).save(mockUser);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userRepo.findById(mockUser.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(mockUser.getId());
        });
        assertEquals("User", exception.getMessage());
    }

    @Test
    void testGetUserByEmail_Success() {
        // Arrange
        when(userRepo.findByEmail(mockUserDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockUser, UserDto.class)).thenReturn(mockUserDto);

        // Act
        UserDto foundUser = userService.getUserByEmail(mockUserDto.getEmail());

        // Assert
        assertNotNull(foundUser);
        assertEquals(mockUserDto.getEmail(), foundUser.getEmail());
    }
}
