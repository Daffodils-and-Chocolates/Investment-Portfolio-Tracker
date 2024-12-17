package com.example.demo.controller;

import com.example.demo.models.dtos.authDto.LoginResponse;
import com.example.demo.models.dtos.authDto.LoginUserDto;
import com.example.demo.models.dtos.authDto.SignUpUserDto;
import com.example.demo.models.dtos.authDto.SignUpResponseDto;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody @Valid SignUpUserDto signUpUserDto) {
        // Call AuthService to handle user registration
        SignUpResponseDto signup = authService.signup(signUpUserDto);

        SignUpResponseDto signupResponse = new SignUpResponseDto();
        signupResponse.setToken(signup.getToken());
        signupResponse.setCreatedUser(signup.getCreatedUser());
        signupResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(signupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        // Authenticate the user using AuthService
        String jwtToken = authService.login(loginUserDto);

        // Generate the LoginResponse with the JWT token
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

//    @PostMapping("/validate-token")
//    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token, @RequestBody UserDetails userDetails) {
//        boolean isValid = authService.validateToken(token, userDetails);
//        return ResponseEntity.ok(isValid);
//    }
}
