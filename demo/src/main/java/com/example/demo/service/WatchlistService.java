package com.example.demo.service;

import com.example.demo.models.entity.Watchlist;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatchlistEntry(Watchlist watchlist);
    Watchlist getWatchlistEntryById(Long id);
    List<Watchlist> getAllWatchlistEntries();
    Watchlist updateWatchlistEntry(Long id, Watchlist watchlist);
    List<Watchlist> getWatchlistByGroup(Long groupId);
    void deleteWatchlistEntry(Long id);
    List<Watchlist> getWatchlistsByUserId(Long userId);
}