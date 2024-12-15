package com.healthcare.user_service.controllers;

import com.healthcare.user_service.payloads.ApiResponse;
import com.healthcare.user_service.payloads.UserDto;
import com.healthcare.user_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createUserDto = this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) {

        UserDto updatedUserDto = this.userService.updateUser(userDto, uid);
        return ResponseEntity.ok(updatedUserDto);
    }


    //ADMIN ONLY
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {
        this.userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully", true), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtoList= this.userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable("userId") Integer uid) {
        UserDto userDto = this.userService.getUserById(uid);
        return ResponseEntity.ok(userDto);
    }



    @GetMapping("/email/{user_email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("user_email") String email) {
        UserDto userDto = this.userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }


    // Admin: Get Pending Doctors
    @GetMapping("/pending/doctors")
    public ResponseEntity<List<UserDto>> getPendingDoctors() {
        List<UserDto> pendingDoctors = userService.getPendingDoctors();
        return ResponseEntity.ok(pendingDoctors);
    }




    // Admin: Approve Doctor
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    @PostMapping("/approve/doctor/{id}")
    public ResponseEntity<UserDto> approveDoctor(@PathVariable Integer id) {
        UserDto approvedDoctor = userService.approveDoctor(id);
        return ResponseEntity.ok(approvedDoctor);
    }

    // Admin: Reject Doctor
    @DeleteMapping("/reject/doctor/{id}")
    @PreAuthorize("hasAuthority('ADMIN_USER')")
    public ResponseEntity<ApiResponse> rejectDoctor(@PathVariable Integer id) {
        userService.rejectDoctor(id);
        return new ResponseEntity<>(new ApiResponse("Doctor rejected successfully", true), HttpStatus.OK);
    }





    @GetMapping("/{userId}/role")
    public ResponseEntity<String> getUserRoleById(@PathVariable("userId") int userId) {
        String roleName = userService.getUserRoleById(userId);
        return ResponseEntity.ok(roleName);  // Return the role name of the user
    }
}
