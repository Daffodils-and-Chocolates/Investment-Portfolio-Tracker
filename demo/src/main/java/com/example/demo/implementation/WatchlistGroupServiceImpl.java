package com.example.demo.implementation;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import com.example.demo.repository.WatchlistGroupRepository;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.service.WatchlistGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WatchlistGroupServiceImpl implements WatchlistGroupService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private WatchlistGroupRepository watchlistGroupRepository;

    @Override
    public WatchlistGroup createWatchlistGroup(WatchlistGroup watchlistGroup) {
        if (watchlistGroupRepository.existsByGroupName(watchlistGroup.getGroupName())) {
            throw new IllegalArgumentException("Group name '" + watchlistGroup.getGroupName() + "' already exists.");
        }
        return watchlistGroupRepository.save(watchlistGroup);
    }

    @Override
    public WatchlistGroup getWatchlistGroupByName(String groupName) {
        return watchlistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new EntityNotFoundException("WatchlistGroup", groupName));
    }

    @Override
    public List<WatchlistGroup> getAllWatchlistGroups() {
        return watchlistGroupRepository.findAll();
    }

    @Override
    public WatchlistGroup updateWatchlistGroup(String groupName, WatchlistGroup watchlistGroupDetails) {
        WatchlistGroup watchlistGroup = getWatchlistGroupByName(groupName);
        watchlistGroup.setGroupName(watchlistGroupDetails.getGroupName());
        watchlistGroup.setGroupDescription(watchlistGroupDetails.getGroupDescription());
        return watchlistGroupRepository.save(watchlistGroup);
    }

    @Override
    public void deleteWatchlistGroup(String groupName) {
        if (!watchlistGroupRepository.existsByGroupName(groupName)) {
            throw new EntityNotFoundException("WatchlistGroup", groupName);
        }
        watchlistGroupRepository.deleteByGroupName(groupName);
    }

    @Override
    public List<Watchlist> addStocksToGroup(String groupName, List<Long> stockIds) {
        WatchlistGroup group = getWatchlistGroupByName(groupName);

        List<Watchlist> addedStocks = new ArrayList<>();
        for (Long stockId : stockIds) {
            Watchlist watchlist = watchlistRepository.findById(stockId)
                    .orElseThrow(() -> new EntityNotFoundException("Stock", stockId));

            watchlist.setGroup(group);
            addedStocks.add(watchlistRepository.save(watchlist));
        }
        return addedStocks;
    }

    @Override
    public List<Watchlist> removeStocksFromGroup(String groupName, List<Long> stockIds) {
        WatchlistGroup group = getWatchlistGroupByName(groupName);

        List<Watchlist> updatedWatchlists = new ArrayList<>();
        for (Long stockId : stockIds) {
            Watchlist watchlist = watchlistRepository.findById(stockId)
                    .orElseThrow(() -> new EntityNotFoundException("Stock", stockId));

            if (watchlist.getGroup() != null && watchlist.getGroup().equals(group)) {
                watchlist.setGroup(null); // Remove association with the group
                updatedWatchlists.add(watchlistRepository.save(watchlist));
            } else {
                throw new IllegalArgumentException(
                        "Stock with ID " + stockId + " is not associated with group '" + groupName + "'."
                );
            }
        }
        return updatedWatchlists;
    }
}
