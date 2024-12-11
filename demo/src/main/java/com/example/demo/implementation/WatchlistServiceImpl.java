package com.example.demo.implementation;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.models.dtos.watchlistDto.WatchlistRequestDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistResponseDto;
import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.User;
import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.WatchlistGroupRepository;
import com.example.demo.repository.WatchlistRepository;
import com.example.demo.service.WatchlistService;
import com.example.demo.util.WatchlistDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistGroupRepository watchlistGroupRepository;
    private final StockRepository stockRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository, WatchlistGroupRepository watchlistGroupRepository, StockRepository stockRepository) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistGroupRepository = watchlistGroupRepository;
        this.stockRepository = stockRepository;
    }

    public WatchlistResponseDto createWatchlist(WatchlistRequestDto watchlistRequestDto, User user) {
        Watchlist newWatchlist = new Watchlist();
        newWatchlist.setUser(user);

        Stock stock = stockRepository.findBySymbol(watchlistRequestDto.getStock().getSymbol())
                .orElseGet(() -> stockRepository.save(watchlistRequestDto.getStock()));
        newWatchlist.setStock(stock);

        WatchlistGroup group = watchlistGroupRepository.findByGroupName(watchlistRequestDto.getGroup().getGroupName())
                .orElseGet(() -> watchlistGroupRepository.save(watchlistRequestDto.getGroup()));
        newWatchlist.setGroup(group);

        return WatchlistDtoMapper.toWatchlistResponseDto(watchlistRepository.save(newWatchlist));
    }

    @Override
    public WatchlistResponseDto getWatchlistById(Long id) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist ", id));
        return WatchlistDtoMapper.toWatchlistResponseDto(watchlist);
    }

    @Override
    public List<WatchlistResponseDto> getAllWatchlists() {
        List<Watchlist> watchlists = watchlistRepository.findAll();
        return watchlists.stream()
                .map(WatchlistDtoMapper::toWatchlistResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WatchlistResponseDto updateWatchlist(Long id, WatchlistRequestDto updatedWatchlistDto) {
        Watchlist existingEntry = watchlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist", id));

        if (updatedWatchlistDto.getStock() != null) {
            Stock stock = stockRepository.findBySymbol(updatedWatchlistDto.getStock().getSymbol())
                    .orElseGet(() -> stockRepository.save(updatedWatchlistDto.getStock()));
            existingEntry.setStock(stock);
        }

        if (updatedWatchlistDto.getGroup() != null) {
            WatchlistGroup group = watchlistGroupRepository.findByGroupName(updatedWatchlistDto.getGroup().getGroupName())
                    .orElseThrow(() -> new EntityNotFoundException("Watchlist group", updatedWatchlistDto.getGroup().getGroupName()));
            existingEntry.setGroup(group);
        }

        Watchlist updatedWatchlist = watchlistRepository.save(existingEntry);

        return WatchlistDtoMapper.toWatchlistResponseDto(updatedWatchlist);
    }



    @Override
    @Transactional
    public void deleteWatchlist(Long id) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist", id));
        watchlistRepository.delete(watchlist);
    }

    public List<Stock> getStocksByUserIdAndGroupName(Long userId, String groupName) {
        return watchlistRepository.findStocksByUserIdAndGroupName(userId, groupName);
    }

    public List<Stock> findAllStocksByUserId(Long userId) {
        return watchlistRepository.findAllStocksByUserId(userId);
    }

    public List<Stock> addStocksToGroup(User user, String groupName, List<Stock> createdStocks) {
        WatchlistGroup group = watchlistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist group", groupName));

        for (Stock stock : createdStocks) {
            Stock resolvedStock;

            // Check if stockId is present
            if (stock.getStockId() != null) {
                resolvedStock = stockRepository.findById(stock.getStockId())
                        .orElseThrow(() -> new EntityNotFoundException("Stock", stock.getStockId().toString()));
            } else {
                // Check if a stock with the same symbol exists
                resolvedStock = stockRepository.findBySymbol(stock.getSymbol())
                        .orElse(stock); // Use the provided stock object if none exists
            }

            Watchlist newEntry = new Watchlist();
            newEntry.setGroup(group);
            newEntry.setUser(user);
            newEntry.setStock(resolvedStock);
            watchlistRepository.save(newEntry);
        }

        return watchlistRepository.findStocksByUserIdAndGroupName(user.getUserId(), group.getGroupName());
    }

    public List<Stock> removeStocksFromGroup(User user, String groupName, List<Long> stockIds) {
        WatchlistGroup group = watchlistGroupRepository.findByGroupName(groupName)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist group", groupName));

        for (Long stockId : stockIds) {
            Watchlist watchlist = watchlistRepository.findByGroupGroupName(groupName)
                    .stream()
                    .filter(w -> w.getStock().getStockId().equals(stockId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Stock not found in group ", groupName));

            boolean isStockReferencedElsewhere = watchlistRepository.existsByStockStockIdAndGroupGroupNameNot(stockId, groupName);

            watchlistRepository.delete(watchlist);

            if (!isStockReferencedElsewhere) {
                stockRepository.delete(watchlist.getStock());  // Assuming you have a stockRepository to delete stocks
            }
        }
        return watchlistRepository.findStocksByUserIdAndGroupName(user.getUserId(), group.getGroupName());
    }

    @Override
    public List<String> getGroupNamesByUserId(Long userId) {
        return watchlistRepository.findDistinctGroupNamesByUserId(userId);
    }

    public List<String> getGroupNamesForStockAndUser(Long userId, Long stockId){
        return watchlistRepository.findGroupNamesByUserIdAndStockId(userId, stockId);
    }

}
