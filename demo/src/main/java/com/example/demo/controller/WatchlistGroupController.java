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
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
                @Operation(summary = "Get a watchlist group by ID", description = "Fetches a watchlist group by its unique ID.")
    public ResponseEntity<WatchlistGroup> getWatchlistGroupById(@PathVariable Long id) {
        WatchlistGroup watchlistGroup = watchlistGroupService.getWatchlistGroupById(id);
        return ResponseEntity.ok(watchlistGroup);
    }

    @GetMapping
    @Operation(summary = "Get all watchlist groups", description = "Retrieves all watchlist groups from the database.")
    public ResponseEntity<List<WatchlistGroup>> getAllWatchlistGroups() {
        List<WatchlistGroup> watchlistGroups = watchlistGroupService.getAllWatchlistGroups();
        return ResponseEntity.ok(watchlistGroups);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a watchlist group by ID", description = "Updates the details of an existing watchlist group.")
    public ResponseEntity<WatchlistGroup> updateWatchlistGroup(
            @PathVariable Long id, @RequestBody WatchlistGroup watchlistGroup) {
        WatchlistGroup updatedGroup = watchlistGroupService.updateWatchlistGroup(id, watchlistGroup);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a stock from watchlist group by ID", description = "Removes a watchlist group from the database by its unique ID.")
    public ResponseEntity<Void> deleteStockWatchlistGroup(@PathVariable Long id) {
        watchlistGroupService.deleteWatchlistGroup(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add multiple stocks to a specific group",
            description = "Assigns multiple stocks to a given watchlist group by providing the group ID and one or more stock IDs."
    )
    @PostMapping("/group/{groupId}/stocks/{stockIds}")
    public List<Watchlist> addStocksToGroup(
            @PathVariable Long groupId,
            @PathVariable List<Long> stockIds
    ) {
        return watchlistGroupService.addStocksToGroup(groupId, stockIds);
    }

    @Operation(
            summary = "Remove a stock from a specific group",
            description = "Detaches a stock from a given watchlist group if it is currently assigned to that group."
    )
    @DeleteMapping("/group/{groupId}/stock/{stockIds}")
    public List<Watchlist> removeStockFromGroup(
            @PathVariable Long groupId,
            @PathVariable List<Long> stockIds
    ) {
        return watchlistGroupService.removeStocksFromGroup(groupId, stockIds);
    }
}