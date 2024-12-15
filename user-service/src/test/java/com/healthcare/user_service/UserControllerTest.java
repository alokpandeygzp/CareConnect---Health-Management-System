package com.healthcare.user_service;

import com.healthcare.user_service.controllers.UserController;
import com.healthcare.user_service.payloads.ApiResponse;
import com.healthcare.user_service.payloads.UserDto;
import com.healthcare.user_service.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UserDto mockUserDto;
    private UserDto mockUpdatedUserDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUserDto = new UserDto();
        mockUserDto.setId(1);
        mockUserDto.setName("John Doe");
        mockUserDto.setEmail("john.doe@example.com");
        mockUserDto.setPassword("password123");

        mockUpdatedUserDto = new UserDto();
        mockUpdatedUserDto.setId(1);
        mockUpdatedUserDto.setName("John Doe Updated");
        mockUpdatedUserDto.setEmail("john.doe.updated@example.com");
        mockUpdatedUserDto.setPassword("updatedPassword123");
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(userService.createUser(mockUserDto)).thenReturn(mockUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.createUser(mockUserDto);

        // Assert
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(mockUserDto.getName(), response.getBody().getName());
        verify(userService, times(1)).createUser(mockUserDto);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        when(userService.updateUser(mockUpdatedUserDto, mockUserDto.getId())).thenReturn(mockUpdatedUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.updateUser(mockUpdatedUserDto, mockUserDto.getId());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUpdatedUserDto.getName(), response.getBody().getName());
        verify(userService, times(1)).updateUser(mockUpdatedUserDto, mockUserDto.getId());
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userService.getUserById(mockUserDto.getId())).thenReturn(mockUserDto);

        // Act
        ResponseEntity<UserDto> response = userController.getSingleUser(mockUserDto.getId());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUserDto.getId(), response.getBody().getId());
        verify(userService, times(1)).getUserById(mockUserDto.getId());
    }

    @Test
    void testGetUsers_Success() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(List.of(mockUserDto));

        // Act
        ResponseEntity<List<UserDto>> response = userController.getUsers();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }
}
