package com.example.demo.service.implementation;

import com.example.demo.exception.notFound.WatchlistNotFoundException;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public Watchlist createWatchlistEntry(Watchlist watchlist) {
        if (watchlist.getSymbol() == null) {
            throw new IllegalArgumentException("Symbol cannot be null");
        }
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist getWatchlistEntryById(Long id) {
        return watchlistRepository.findById(id)
                .orElseThrow(() -> new WatchlistNotFoundException(id));
    }

    @Override
    public List<Watchlist> getAllWatchlistEntries() {
        return watchlistRepository.findAll();
    }

    @Override
    public List<Watchlist> getWatchlistByGroup(Long groupId) {
        return watchlistRepository.findWatchlistsByGroupId(groupId);
    }

    @Override
    public List<Watchlist> getWatchlistsByUserId(Long userId) {
        return watchlistRepository.findWatchlistsByUserId(userId);  // New method
    }

    @Override
    public Watchlist updateWatchlistEntry(Long id, Watchlist updatedWatchlist) {
        Watchlist existingEntry = getWatchlistEntryById(id);

        existingEntry.setSymbol(updatedWatchlist.getSymbol());
        existingEntry.setDescription(updatedWatchlist.getDescription());

        if (updatedWatchlist.getGroups() != null) {
            existingEntry.getGroups().clear();
            for (WatchlistGroup group : updatedWatchlist.getGroups()) {
                group.setWatchlist(existingEntry);
            }

            existingEntry.getGroups().addAll(updatedWatchlist.getGroups());
        }
        return watchlistRepository.save(existingEntry);
    }


    @Override
    @Transactional
    public void deleteWatchlistEntry(Long id) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new WatchlistNotFoundException(id));

        // Break bidirectional association
        User user = watchlist.getUser();
        if (user != null) {
            user.setWatchlist(null);
        }

        watchlistRepository.deleteById(id);
    }
}
