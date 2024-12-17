package com.example.demo.models.dtos.authDto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginUserDto {
    @Email(message = "Please provide a valid email address")
    private String email;
    private String password;
}