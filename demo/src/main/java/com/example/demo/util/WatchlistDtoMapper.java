package com.example.demo.util;

import com.example.demo.models.dtos.watchlistDto.StockDto;
import com.example.demo.models.dtos.watchlistDto.UserDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistGroupDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistResponseDto;
import com.example.demo.models.entity.Watchlist;

public class WatchlistDtoMapper {

    // Method to convert Watchlist entity to WatchlistResponseDto
    public static WatchlistResponseDto toWatchlistResponseDto(Watchlist watchlist) {
        WatchlistResponseDto responseDto = new WatchlistResponseDto();
        responseDto.setWatchlistId(watchlist.getWatchlistId());

        // Map user details
        UserDto userDto = new UserDto();
        userDto.setUserId(watchlist.getUser().getUserId());
        userDto.setEmail(watchlist.getUser().getEmail());
        userDto.setName(watchlist.getUser().getName());
        responseDto.setUser(userDto);

        // Map stock details
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(watchlist.getStock().getSymbol());
        responseDto.setStock(stockDto);

        // Map watchlist group details
        WatchlistGroupDto groupDto = new WatchlistGroupDto();
        groupDto.setGroupName(watchlist.getGroup().getGroupName());
        responseDto.setGroup(groupDto);

        return responseDto;
    }
}
