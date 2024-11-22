package com.example.demo.controller;

import com.example.demo.models.dtos.LoginResponse;
import com.example.demo.models.dtos.LoginUserDto;
import com.example.demo.models.dtos.RegisterUserDto;
import com.example.demo.models.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterUserDto registerUserDto) {
        // Call AuthService to handle user registration
        User registeredUser = authService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto loginUserDto) {
        // Authenticate the user using AuthService
        String jwtToken = authService.login(loginUserDto);

        // Generate the LoginResponse with the JWT token
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    // Add a token validation endpoint if you want to validate JWT tokens directly.
    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token, @RequestBody UserDetails userDetails) {
        boolean isValid = authService.validateToken(token, userDetails);
        return ResponseEntity.ok(isValid);
    }
}