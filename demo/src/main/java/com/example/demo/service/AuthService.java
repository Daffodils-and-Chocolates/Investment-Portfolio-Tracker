package com.example.demo.service;
import com.example.demo.models.dtos.authDto.SignUpResponseDto;
import com.example.demo.models.dtos.authDto.LoginUserDto;
import com.example.demo.models.dtos.authDto.SignUpUserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    String login(LoginUserDto loginUserDto);
    boolean validateToken(String token, UserDetails userDetails);
    String extractUsernameFromToken(String token);
    SignUpResponseDto signup(SignUpUserDto signUpUserDto);
}
