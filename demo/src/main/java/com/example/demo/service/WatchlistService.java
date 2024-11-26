package com.example.demo.service;

import com.example.demo.models.dtos.watchlistDto.WatchlistRequestDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistResponseDto;
import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.User;

import java.util.List;

public interface WatchlistService {
    WatchlistResponseDto createWatchlist(WatchlistRequestDto watchlistRequestDto, User user);
    WatchlistResponseDto getWatchlistById(Long id);
    List<WatchlistResponseDto> getAllWatchlists();
    WatchlistResponseDto updateWatchlist(Long id, WatchlistRequestDto updatedWatchlistDto);
    void deleteWatchlist(Long id);
    List<Stock> getStocksByUserIdAndGroupName(Long userId, String groupName);
    List<Stock> findAllStocksByUserId(Long userId);
    List<String> getGroupNamesByUserId(Long userId);
    List<Stock> addStocksToGroup(User user, String groupName, List<Stock> createdStocks);
    List<Stock> removeStocksFromGroup(User user, String groupName, List<Long> stockIds);
    List<String> getGroupNamesForStockAndUser(Long userId, Long stockId);
}
