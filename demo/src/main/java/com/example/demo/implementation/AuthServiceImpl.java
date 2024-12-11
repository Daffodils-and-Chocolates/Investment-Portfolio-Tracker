package com.example.demo.implementation;

import com.example.demo.models.dtos.authDto.LoginUserDto;
import com.example.demo.models.dtos.authDto.SignUpUserDto;
import com.example.demo.models.dtos.authDto.SignUpResponseDto;
import com.example.demo.models.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtService;
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
    private final UserService userService;

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
    public SignUpResponseDto signup(SignUpUserDto signUpUserDto) {
        User newUser = new User();
        newUser.setEmail(signUpUserDto.getEmail());
        newUser.setPassword(signUpUserDto.getPassword());
        newUser.setName(signUpUserDto.getName());
        userService.createUser(newUser);
        return new SignUpResponseDto(jwtService.generateToken(newUser), newUser,null);
    }

}
