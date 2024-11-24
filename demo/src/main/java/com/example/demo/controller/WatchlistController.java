package com.example.demo.controller;

import com.example.demo.models.dtos.watchlistDto.WatchlistRequestDto;
import com.example.demo.models.dtos.watchlistDto.WatchlistResponseDto;
import com.example.demo.models.entity.Stock;
import com.example.demo.models.entity.User;
import com.example.demo.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "Manage Watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Operation(summary = "Add a new stock to the watchlist", description = "Allows the user to add a stock to their watchlist...")
    @PostMapping
    public WatchlistResponseDto createWatchlistEntry(@RequestBody WatchlistRequestDto watchlist, @AuthenticationPrincipal User user) {
        return watchlistService.createWatchlist(watchlist, user);
    }

    @Operation(summary = "Retrieve a watchlist entry by ID", description = "Fetch a specific stock...")
    @GetMapping("/{id}")
    public WatchlistResponseDto getWatchlistEntryById(@PathVariable Long id) {
        return watchlistService.getWatchlistById(id);
    }

    @Operation(summary = "Get all stocks in the watchlist", description = "This endpoint retrieves all stocks...")
    @GetMapping
    public List<WatchlistResponseDto> getAllWatchlistEntries() {
        return watchlistService.getAllWatchlists();
    }

    @Operation(summary = "Retrieve stocks by group name by the user", description = "Retrieve all stocks that belong...")
    @GetMapping("/group/{groupName}")
    public List<Stock> getStocksByUserAndGroupName(@AuthenticationPrincipal User user, @PathVariable String groupName) {
        return watchlistService.getStocksByUserIdAndGroupName(user.getUserId(), groupName);
    }

    @Operation(summary = "Update a watchlist entry by ID", description = "This endpoint allows the user to update an existing...")
    @PutMapping("/{id}")
    public WatchlistResponseDto updateWatchlistEntry(@PathVariable Long id, @RequestBody WatchlistRequestDto watchlist) {
        return watchlistService.updateWatchlist(id, watchlist);
    }

    @Operation(summary = "Delete a watchlist entry by ID", description = "This operation deletes...")
    @DeleteMapping("/{id}")
    public void deleteWatchlistEntry(@PathVariable Long id) {
        watchlistService.deleteWatchlist(id);
    }

    @Operation(summary = "Add stocks to a group", description = "Allows a user to add multiple stocks to a specified watchlist group...")
    @PostMapping("/{groupName}/add-stocks")
    public ResponseEntity<List<Stock>> addStocksToGroup(@PathVariable String groupName,
                                                        @RequestBody List<Stock> stocks,
                                                        @AuthenticationPrincipal User user) {
        List<Stock> updatedStocks = watchlistService.addStocksToGroup(user, groupName, stocks);
        return ResponseEntity.ok(updatedStocks);
    }

    @Operation(summary = "Remove stocks from a group", description = "Allows a user to remove multiple stocks...")
    @DeleteMapping("/{groupName}/remove-stocks")
    public ResponseEntity<List<Stock>> removeStocksFromGroup(@RequestBody List<Long> stockIds,
                                                             @PathVariable String groupName,
                                                             @AuthenticationPrincipal User user) {
        List<Stock> updatedStocks = watchlistService.removeStocksFromGroup(user, groupName, stockIds);
        return ResponseEntity.ok(updatedStocks);
    }
}
