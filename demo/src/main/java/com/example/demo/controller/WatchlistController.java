package com.example.demo.controller;

import com.example.demo.models.entity.Watchlist;
import com.example.demo.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "Manage Watchlists")
public class WatchlistController {

//    @Autowired
    private final WatchlistService watchlistService;

    @Operation(
            summary = "Add a new stock to the watchlist",
            description = "Allows the user to add a stock to their watchlist. includes : a group, a symbol, description, and display symbol for easy identification."
    )
    @PostMapping
    public Watchlist createWatchlistEntry(@RequestBody Watchlist watchlist) {
        return watchlistService.createWatchlistEntry(watchlist);
    }

    @Operation(
            summary = "Retrieve a watchlist entry by ID",
            description = "Fetch a specific stock from the watchlist by its unique ID. This operation is useful when the user needs to view or modify a particular stock's details."
    )
    @GetMapping("/{id}")
    public Watchlist getWatchlistEntryById(@PathVariable Long id) {
        return watchlistService.getWatchlistEntryById(id);
    }

    @Operation(
            summary = "Get all stocks in the watchlist",
            description = "This endpoint retrieves all the stocks that are currently in the watchlist. It returns a list of all stocks, including details such as the symbol, group association, and description."
    )
    @GetMapping
    public List<Watchlist> getAllWatchlistEntries() {
        return watchlistService.getAllWatchlistEntries();
    }

    @Operation(
            summary = "Retrieve stocks by group ID",
            description = "Retrieve all stocks that belong to a specific group within the watchlist. Groups allow users to categorize their watchlist, making it easier to manage multiple stocks."
    )
    @GetMapping("/group/{groupId}")
    public List<Watchlist> getWatchlistByGroup(@PathVariable Long groupId) {
        return watchlistService.getWatchlistByGroup(groupId);
    }

    @Operation(
            summary = "Retrieve a user's watchlist based on user ID"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Watchlist>> getWatchlistsByUserId(@PathVariable Long userId) {
        List<Watchlist> watchlists = watchlistService.getWatchlistsByUserId(userId);
        if (watchlists.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(watchlists, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a watchlist entry by ID",
            description = "This endpoint allows the user to update an existing stock entry in the watchlist. The user can modify the stock symbol, description, and group association."
    )
    @PutMapping("/{id}")
    public Watchlist updateWatchlistEntry(@PathVariable Long id, @RequestBody Watchlist watchlist) {
        return watchlistService.updateWatchlistEntry(id, watchlist);
    }

    @Operation(
            summary = "Delete a watchlist entry by ID",
            description = "This operation deletes a stock from the watchlist based on its unique ID. Once deleted, the stock will no longer appear in the user's watchlist."
    )
    @DeleteMapping("/{id}")
    public void deleteWatchlistEntry(@PathVariable Long id) {
        watchlistService.deleteWatchlistEntry(id);
    }
}