package com.example.demo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class GetUserDto {
    private String email;

    private String name;

}
