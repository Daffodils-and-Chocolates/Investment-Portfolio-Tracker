package com.example.demo.models.dtos.watchlistDto;

import com.example.demo.models.entity.Stock;
import lombok.Data;

@Data
public class WatchlistRequestDto {
    private Stock stock;
    private WatchlistGroupDto group;
}