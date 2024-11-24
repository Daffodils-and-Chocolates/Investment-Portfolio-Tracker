package com.example.demo.models.dtos.authDto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long expiresIn;
}