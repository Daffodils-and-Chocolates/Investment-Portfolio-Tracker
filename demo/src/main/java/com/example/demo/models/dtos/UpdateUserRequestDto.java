package com.example.demo.models.dtos;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String name;
    private String email;
    private String oldPassword;
    private String newPassword;
}
