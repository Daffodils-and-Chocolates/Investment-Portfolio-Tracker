package com.example.demo.service;

import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import java.util.List;

public interface WatchlistGroupService {
    WatchlistGroup createWatchlistGroup(WatchlistGroup watchlistGroup);
    WatchlistGroup getWatchlistGroupById(Long id);
    List<WatchlistGroup> getAllWatchlistGroups();
    WatchlistGroup updateWatchlistGroup(Long id, WatchlistGroup watchlistGroup);
    void deleteWatchlistGroup(Long id);

    //Watchlist stocks
    List<Watchlist> addStocksToGroup(Long groupId, List<Long> stockIds);
    List<Watchlist> removeStocksFromGroup(Long groupId, List<Long> stockIds);
}