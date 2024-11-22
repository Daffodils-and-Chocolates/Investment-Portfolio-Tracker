package com.example.demo.service;
import com.example.demo.models.entity.User;
import com.example.demo.models.dtos.LoginUserDto;
import com.example.demo.models.dtos.RegisterUserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    String login(LoginUserDto loginUserDto);
    boolean validateToken(String token, UserDetails userDetails);
    String extractUsernameFromToken(String token);
    User signup(RegisterUserDto registerUserDto);
}
