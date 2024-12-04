package com.example.demo.controller;

import com.example.demo.config.FinnhubWebSocketClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/finnhub")
public class FinnhubController {

    private final FinnhubWebSocketClient webSocketClient;

    @GetMapping("/stream")
    public SseEmitter streamMarketData() {
        return webSocketClient.subscribe();
    }

    @PostMapping("/subscribe")
    public void subscribeToSymbols(@RequestBody List<String> symbols) {
        webSocketClient.subscribeToSymbols(symbols);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromSymbols(@RequestBody List<String> symbols){
        webSocketClient.unsubscribeFromSymbols(symbols);
    }
}
