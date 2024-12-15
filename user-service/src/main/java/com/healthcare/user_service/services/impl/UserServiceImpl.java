package com.healthcare.user_service.services.impl;

import com.healthcare.user_service.config.AppConstants;
import com.healthcare.user_service.entities.Role;
import com.healthcare.user_service.entities.User;
import com.healthcare.user_service.exceptions.ApiException;
import com.healthcare.user_service.exceptions.ResourceNotFoundException;
import com.healthcare.user_service.payloads.UserDto;
import com.healthcare.user_service.repositories.RoleRepo;
import com.healthcare.user_service.repositories.UserRepo;
import com.healthcare.user_service.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);

        //Set encoded password
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        //Set roles -> New user has normal role only
        Role role= this.roleRepo.findById(AppConstants.NORMAL_USER).get();
        user.setRole(role);

        User newUser = this.userRepo.save(user);
        return this.modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        //User savedUser = this.userRepo.save(userDto);
        //This will not work, "save" has User datatype in JPARepository function

        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id", userId));
        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        List<UserDto> userDtos= users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
        this.userRepo.delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Email : " + email, 0));

        System.out.println(user.getName());
        System.out.println(user.getPassword());
        System.out.println(user.getRole().getName()+"\t"+user.getRole().getId());
        System.out.println(user.getEmail());

        return this.modelMapper.map(user,UserDto.class);
    }


    // Register Patient
    @Override
    public UserDto registerPatient(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // Assign role as PATIENT
        Role patientRole = this.roleRepo.findByName("NORMAL_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "NORMAL_USER", AppConstants.NORMAL_USER));
        user.setRole(patientRole);
        user.setStatus("ACTIVE");

        User savedUser = userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    // Register Doctor (Pending Approval)
    @Override
    public UserDto registerDoctor(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));

        // Assign role as DOCTOR
        Role doctorRole = this.roleRepo.findByName("DOCTOR_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "DOCTOR_USER", AppConstants.DOCTOR_USER));
        user.setRole(doctorRole);
        user.setStatus("PENDING");

        User savedUser = userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    // Get Pending Doctors
    @Override
    public List<UserDto> getPendingDoctors() {
        List<User> pendingDoctors = userRepo.findByStatus("PENDING");
        return pendingDoctors.stream().map(user -> this.modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    // Approve Doctor
    @Override
    public UserDto approveDoctor(Integer userId) {
        System.out.println("************AA GYA0*************");
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if (!user.getRole().getName().equals("DOCTOR_USER") || !user.getStatus().equals("PENDING")) {
            throw new ApiException("Invalid user or status for approval");
        }

        System.out.println("************AA GYA1*************");
        user.setStatus("ACTIVE");
        User approvedUser = userRepo.save(user);
        return this.modelMapper.map(approvedUser, UserDto.class);
    }

    // Reject Doctor
    @Override
    public void rejectDoctor(Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        if (!user.getRole().getName().equals("DOCTOR_USER") || !user.getStatus().equals("PENDING")) {
            throw new ApiException("Invalid user or status for rejection");
        }

        userRepo.delete(user);
    }


    @Override
    public boolean emailExists(String email) {
        return this.userRepo.findByEmail(email).isPresent();
    }

    // New method to get the role by user ID
    public String getUserRoleById(int userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ApiException("User not found"));
        return user.getRole().getName();  // Return the name of the role associated with the user
    }

    public User dtoToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    public UserDto userToDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }
}
