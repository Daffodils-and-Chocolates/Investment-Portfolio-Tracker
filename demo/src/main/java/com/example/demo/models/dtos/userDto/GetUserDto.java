package com.example.demo.models.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class GetUserDto {
    private String email;
    private String name;

}
