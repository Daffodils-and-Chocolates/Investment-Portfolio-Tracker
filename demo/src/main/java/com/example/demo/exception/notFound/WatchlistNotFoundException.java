package com.example.demo.exception.notFound;

public class WatchlistNotFoundException extends RuntimeException {
    public WatchlistNotFoundException(Long id) {
        super("ERROR :\nWatchlist with ID " + id + " not found.");
    }
}