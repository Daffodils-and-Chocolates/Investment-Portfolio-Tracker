package com.example.demo.models.dtos;

import lombok.Data;

@Data
public class SignUpUserDto {
    private String email;

    private String password;

    private String name;
}