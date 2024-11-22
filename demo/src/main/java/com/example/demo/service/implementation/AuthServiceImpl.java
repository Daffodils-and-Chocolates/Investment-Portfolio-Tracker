package com.example.demo.service.implementation;

import com.example.demo.models.dtos.LoginUserDto;
import com.example.demo.models.dtos.RegisterUserDto;
import com.example.demo.models.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;  // Assuming UserService handles user CRUD operations

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    public String login(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginUserDto.getEmail());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return jwtService.isTokenValid(token, userDetails);
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return jwtService.extractUsername(token);
    }

    @Override
    public User signup(RegisterUserDto registerUserDto) {
        // Convert RegisterUserDto to User entity and save it using UserService
        User newUser = new User();
        newUser.setEmail(registerUserDto.getEmail());
        newUser.setPassword(registerUserDto.getPassword());  // Assuming password encryption handled in UserService
        newUser.setName(registerUserDto.getName());  // Setting name instead of fullName
        return userService.createUser(newUser);
    }

}
