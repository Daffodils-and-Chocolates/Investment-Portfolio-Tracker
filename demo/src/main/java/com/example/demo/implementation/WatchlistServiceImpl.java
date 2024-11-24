package com.example.demo.implementation;

import com.example.demo.exception.notFound.WatchlistNotFoundException;
import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import com.example.demo.repository.WatchlistGroupRepository;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.service.WatchlistService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistGroupRepository watchlistGroupRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository,WatchlistGroupRepository watchlistGroupRepository ) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistGroupRepository = watchlistGroupRepository;
    }

    @Override
    public Watchlist createWatchlist(Watchlist watchlist) {
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist getWatchlistById(Long id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist not found with id: " + id));
    }

    @Override
    public List<Watchlist> getAllWatchlists() {
        return watchlistRepository.findAll();
    }

    @Override
    @Transactional
    public Watchlist updateWatchlist(Long id, Watchlist updatedWatchlist) {
        // Fetch the existing Watchlist entry by ID
        Watchlist existingEntry = watchlistRepository.findById(id)
                .orElseThrow(() -> new WatchlistNotFoundException(id));

        // Update the fields (adjust to the fields available in Watchlist)
        existingEntry.setStock(updatedWatchlist.getStock());

        if (updatedWatchlist.getGroup() != null) {
            WatchlistGroup group = watchlistGroupRepository.findById(updatedWatchlist.getGroup().getGroupId())
                    .orElseThrow(() -> new EntityNotFoundException("Could not find Watchlist group with id : "+updatedWatchlist.getGroup().getGroupId()));
            existingEntry.setGroup(group);
        }

        return watchlistRepository.save(existingEntry);
    }

    @Override
    @Transactional
    public void deleteWatchlist(Long id) {
        // Fetch the Watchlist entry by ID
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new WatchlistNotFoundException(id));

        if (watchlist.getGroup() != null) {
            watchlist.setGroup(null);
        }

        watchlistRepository.delete(watchlist);
    }
}

    public List<Stock> getStocksByUserIdAndGroupName(Long userId, String groupName) {
        return watchlistRepository.findStocksByUserIdAndGroupName(userId, groupName);
    }

    public List<Stock> findAllStocksByUserId(Long userId) {
        return watchlistRepository.findAllStocksByUserId(userId);
    }
}
