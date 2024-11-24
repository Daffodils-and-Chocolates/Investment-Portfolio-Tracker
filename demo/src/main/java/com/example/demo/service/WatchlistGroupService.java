package com.example.demo.service;

import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import java.util.List;

public interface WatchlistGroupService {
    WatchlistGroup createWatchlistGroup(WatchlistGroup watchlistGroup);
    WatchlistGroup getWatchlistGroupByName(String groupName);
    List<WatchlistGroup> getAllWatchlistGroups();
    WatchlistGroup updateWatchlistGroup(String groupName, WatchlistGroup watchlistGroupDetails);
    void deleteWatchlistGroup(String groupName);
    List<Watchlist> addStocksToGroup(String groupName, List<Long> stockIds);
    List<Watchlist> removeStocksFromGroup(String groupName, List<Long> stockIds);
}
