package com.example.demo.controller;

import com.example.demo.models.entity.Watchlist;
import com.example.demo.models.entity.WatchlistGroup;
import com.example.demo.service.WatchlistGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist-groups")
@Tag(name = "WatchlistGroup", description = "Manage watchlist groups")
public class WatchlistGroupController {

    @Autowired
    private WatchlistGroupService watchlistGroupService;

    @PostMapping
    @Operation(summary = "Create a new watchlist group", description = "Adds a new watchlist group to the database.")
    public ResponseEntity<WatchlistGroup> createWatchlistGroup(@RequestBody WatchlistGroup watchlistGroup) {
        WatchlistGroup createdGroup = watchlistGroupService.createWatchlistGroup(watchlistGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{groupName}")
    @Operation(summary = "Get a watchlist group by name", description = "Fetches a watchlist group by its unique name.")
    public ResponseEntity<WatchlistGroup> getWatchlistGroupByName(@PathVariable String groupName) {
        WatchlistGroup watchlistGroup = watchlistGroupService.getWatchlistGroupByName(groupName);
        return ResponseEntity.ok(watchlistGroup);
    }

    @GetMapping
    @Operation(summary = "Get all watchlist groups", description = "Retrieves all watchlist groups from the database.")
    public ResponseEntity<List<WatchlistGroup>> getAllWatchlistGroups() {
        List<WatchlistGroup> watchlistGroups = watchlistGroupService.getAllWatchlistGroups();
        return ResponseEntity.ok(watchlistGroups);
    }

    @PutMapping("/{groupName}")
    @Operation(summary = "Update a watchlist group by name", description = "Updates the details of an existing watchlist group.")
    public ResponseEntity<WatchlistGroup> updateWatchlistGroup(
            @PathVariable String groupName,
            @RequestBody WatchlistGroup watchlistGroupDetails) {
        WatchlistGroup updatedGroup = watchlistGroupService.updateWatchlistGroup(groupName, watchlistGroupDetails);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{groupName}")
    @Operation(summary = "Delete a watchlist group by name", description = "Removes a watchlist group from the database by its unique name.")
    public ResponseEntity<Void> deleteWatchlistGroup(@PathVariable String groupName) {
        watchlistGroupService.deleteWatchlistGroup(groupName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupName}/stocks")
    @Operation(summary = "Add stocks to a watchlist group", description = "Associates stocks with an existing watchlist group by its name.")
    public ResponseEntity<List<Watchlist>> addStocksToGroup(
            @PathVariable String groupName,
            @RequestBody List<Long> stockIds) {
        List<Watchlist> addedStocks = watchlistGroupService.addStocksToGroup(groupName, stockIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedStocks);
    }

    @DeleteMapping("/{groupName}/stocks")
    @Operation(summary = "Remove stocks from a watchlist group", description = "Disassociates stocks from an existing watchlist group by its name.")
    public ResponseEntity<List<Watchlist>> removeStocksFromGroup(
            @PathVariable String groupName,
            @RequestBody List<Long> stockIds) {
        List<Watchlist> updatedWatchlists = watchlistGroupService.removeStocksFromGroup(groupName, stockIds);
        return ResponseEntity.ok(updatedWatchlists);
    }
}
