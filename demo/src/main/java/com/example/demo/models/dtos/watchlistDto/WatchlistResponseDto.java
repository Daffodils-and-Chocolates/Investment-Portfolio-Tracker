package com.example.demo.models.dtos.watchlistDto;

import com.example.demo.models.entity.Stock;
import lombok.Data;

@Data
public class WatchlistResponseDto {
    private Long watchlistId;
    private UserDto user;
    private Stock stock;
    private WatchlistGroupDto group;
}