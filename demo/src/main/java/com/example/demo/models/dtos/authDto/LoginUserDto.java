package com.example.demo.models.dtos.authDto;

import lombok.Data;

@Data
public class LoginUserDto {
    private String email;
    private String password;
}