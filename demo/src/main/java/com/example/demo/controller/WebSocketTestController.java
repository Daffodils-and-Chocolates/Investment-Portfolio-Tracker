package com.example.demo.controller;

import com.example.demo.util.FinnhubWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class WebSocketTestController {

    @Autowired
    private FinnhubWebSocketService finnhubWebSocketService;

    @GetMapping("/test-websocket")
    public String testWebSocketConnection() {
        finnhubWebSocketService.connectToFinnhub();
        return "WebSocket connection initiated!";
    }

//    @GetMapping(value = "/stock-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter streamStockData() {
//        // Create an emitter to send stock data to the frontend
//    }

//    @GetMapping("/close-websocket")
//    public String closeWebSocketConnection(){
//        finnhubWebSocketService.
//    }
}