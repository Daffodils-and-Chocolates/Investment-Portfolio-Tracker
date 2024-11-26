package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    private final FinnhubWebSocketClient webSocketClient;

    public WebSocketConfig(FinnhubWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @PostConstruct
    public void init() {
        webSocketClient.connect();
    }
}