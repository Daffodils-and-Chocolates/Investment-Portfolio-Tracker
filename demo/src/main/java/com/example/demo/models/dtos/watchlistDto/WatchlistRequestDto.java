package com.example.demo.models.dtos.watchlistDto;

import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.WatchlistGroup;
import lombok.Data;

@Data
public class WatchlistRequestDto {
    private Stock stock;
    private WatchlistGroup group;
}