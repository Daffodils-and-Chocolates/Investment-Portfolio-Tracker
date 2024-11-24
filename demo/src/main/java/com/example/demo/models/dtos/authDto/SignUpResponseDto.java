package com.example.demo.models.dtos.authDto;

import com.example.demo.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {
    private String token;
    private User createdUser;
    private Long expiresIn;
}
