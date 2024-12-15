package com.healthcare.user_service.controllers;

import com.healthcare.user_service.entities.User;
import com.healthcare.user_service.exceptions.ApiException;
import com.healthcare.user_service.payloads.ApiResponse;
import com.healthcare.user_service.payloads.JwtAuthRequest;
import com.healthcare.user_service.payloads.JwtAuthResponse;
import com.healthcare.user_service.payloads.UserDto;
import com.healthcare.user_service.security.JwtTokenHelper;
import com.healthcare.user_service.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {


    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request
            ) {

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        UserDto userdto = userService.getUserByEmail(request.getUsername());
        // Create claims for JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userdto.getId());
        claims.put("name", userdto.getName());
        claims.put("role", userdto.getRole().getName());
        claims.put("email", userdto.getEmail());

        String generatedToken = this.jwtTokenHelper.generateToken(userDetails, claims);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(generatedToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(username, password);

        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException ex) {
            System.out.println("Invalid Details !");
            throw new ApiException("Invalid username or password !!");
        }
    }



    // Register Patient
    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody UserDto userDto) {
        // Check if the email already exists
        if (userService.emailExists(userDto.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Email already exists", false), HttpStatus.BAD_REQUEST);
        }

        UserDto registeredUser  = userService.registerPatient(userDto);
        return new ResponseEntity<>(registeredUser , HttpStatus.CREATED);
    }

    // Register Doctor
    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody UserDto userDto) {
        // Check if the email already exists
        if (userService.emailExists(userDto.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("Email already exists", false), HttpStatus.BAD_REQUEST);
        }

        UserDto registeredUser  = userService.registerDoctor(userDto);
        return new ResponseEntity<>(registeredUser , HttpStatus.CREATED);
    }


//    //register new user api
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
//        // Check if the email already exists
//        Optional<User> existingUser = this.userRepo.findByEmail(userDto.getEmail());
//        if (existingUser.isPresent()) {
//            // Return a response indicating the email is already taken
//            return new ResponseEntity<>(new ApiResponse("Email already exists", false), HttpStatus.BAD_REQUEST);
//        }
//
//        // If the email is available, proceed to register the new user
//        UserDto registeredUser = this.userService.registerNewUser(userDto);
//        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
//    }



//    @PostMapping("/register")
//    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
//        UserDto registeredUser = this.userService.registerNewUser(userDto);
//        return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
//    }
}
