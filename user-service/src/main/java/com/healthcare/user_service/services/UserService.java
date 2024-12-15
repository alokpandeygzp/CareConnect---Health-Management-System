package com.healthcare.user_service.services;

import com.healthcare.user_service.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerNewUser(UserDto userDto);
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
    UserDto getUserByEmail(String email);


    boolean emailExists(String email);
    UserDto registerPatient(UserDto userDto);
    UserDto registerDoctor(UserDto userDto);
    List<UserDto> getPendingDoctors();
    UserDto approveDoctor(Integer userId);
    void rejectDoctor(Integer userId);

    String getUserRoleById(int userId);
}
