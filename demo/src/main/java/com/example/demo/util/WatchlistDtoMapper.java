package com.example.demo.util;

import com.example.demo.models.dtos.watchlistDto.UserDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistGroupDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistResponseDto;
import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.Watchlist;

public class WatchlistDtoMapper {
    public static WatchlistResponseDto toWatchlistResponseDto(Watchlist watchlist) {
        WatchlistResponseDto responseDto = new WatchlistResponseDto();
        responseDto.setWatchlistId(watchlist.getWatchlistId());

        UserDto userDto = new UserDto();
        userDto.setUserId(watchlist.getUser().getUserId());
        userDto.setEmail(watchlist.getUser().getEmail());
        userDto.setName(watchlist.getUser().getName());
        responseDto.setUser(userDto);

        responseDto.setStock(watchlist.getStock());

        WatchlistGroupDto groupDto = new WatchlistGroupDto();
        groupDto.setGroupName(watchlist.getGroup().getGroupName());
        responseDto.setGroup(groupDto);

        return responseDto;
    }
}
