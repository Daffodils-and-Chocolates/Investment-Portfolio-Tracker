package com.example.demo.service.implementation;

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
        return watchlistGroupRepository.save(watchlistGroup);
    }

    @Override
    public WatchlistGroup getWatchlistGroupById(Long id) {
        return watchlistGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WatchlistGroup not found with id " + id));
    }

    @Override
    public List<WatchlistGroup> getAllWatchlistGroups() {
        return watchlistGroupRepository.findAll();
    }

    @Override
    public WatchlistGroup updateWatchlistGroup(Long id, WatchlistGroup watchlistGroupDetails) {
        WatchlistGroup watchlistGroup = getWatchlistGroupById(id);
        watchlistGroup.setName(watchlistGroupDetails.getName());
        return watchlistGroupRepository.save(watchlistGroup);
    }

    @Override
    public void deleteWatchlistGroup(Long id) {
        watchlistGroupRepository.deleteById(id);
    }

    public List<Watchlist> addStocksToGroup(Long groupId, List<Long> stockIds) {
        WatchlistGroup group = watchlistGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Watchlist> addedStocks = new ArrayList<>();
        for (Long stockId : stockIds) {
            Watchlist watchlist = watchlistRepository.findById(stockId)
                    .orElseThrow(() -> new RuntimeException("Stock with ID " + stockId + " not found"));
            watchlist.setGroup(group);
            addedStocks.add(watchlistRepository.save(watchlist));
        }
        return addedStocks;
    }

    @Override
    public List<Watchlist> removeStocksFromGroup(Long groupId, List<Long> stockIds) {
        List<Watchlist> updatedWatchlists = new ArrayList<>();

        for (Long stockId : stockIds) {
            Watchlist watchlist = watchlistRepository.findById(stockId)
                    .orElseThrow(() -> new RuntimeException("Stock with ID " + stockId + " not found"));

            // Check if the stock is associated with the given group
            if (watchlist.getGroup() != null && watchlist.getGroup().getId().equals(groupId)) {
                watchlist.setGroup(null);  // Remove association with the group
                updatedWatchlists.add(watchlistRepository.save(watchlist));
            } else {
                throw new RuntimeException("Stock with ID " + stockId + " is not associated with the specified group");
            }
        }
        return updatedWatchlists;  // Return the list of updated watchlists 
    }

}