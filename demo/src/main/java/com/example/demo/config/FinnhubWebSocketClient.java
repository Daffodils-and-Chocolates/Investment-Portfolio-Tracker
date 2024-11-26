package com.example.demo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class FinnhubWebSocketClient extends TextWebSocketHandler {
    private WebSocketSession webSocketSession;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final String FINNHUB_WS_URL = "wss://ws.finnhub.io?token=csomuhpr01qt3r34doe0csomuhpr01qt3r34doeg";

    public void connect() {
        WebSocketClient client = new StandardWebSocketClient();
        try {
            webSocketSession = client.execute(this, FINNHUB_WS_URL).get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Finnhub WebSocket", e);
        }
    }

    public void subscribeToSymbols(List<String> symbols) {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            symbols.forEach(symbol -> {
                try {
                    String subscribeMessage = String.format("{\"type\":\"subscribe\",\"symbol\":\"%s\"}", symbol);
                    webSocketSession.sendMessage(new TextMessage(subscribeMessage));
                    System.out.println("Subscribed successfully to "+ symbol);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to subscribe to symbol: " + symbol, e);
                }
            });
        }
    }

    public void unsubscribeFromSymbols(List<String> symbols) {
        if (webSocketSession != null && webSocketSession.isOpen()) {
            symbols.forEach(symbol -> {
                try {
                    String unsubscribeMessage = String.format("{\"type\":\"unsubscribe\",\"symbol\":\"%s\"}", symbol);
                    webSocketSession.sendMessage(new TextMessage(unsubscribeMessage));
                    System.out.println("Unsubscribed successfully from " + symbol);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to unsubscribe from symbol: " + symbol, e);
                }
            });
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // Parse the message payload
            String payload = message.getPayload();
            if (payload.contains("\"type\":\"ping\"")) {
                String pongMessage = "{\"type\":\"pong\"}";
                session.sendMessage(new TextMessage(pongMessage));
            } else {
                // Forward the message to all connected SSE clients
                emitters.removeIf(emitter -> {
                    try {
                        emitter.send(payload);
                        return false;
                    } catch (IOException e) {
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error handling message: " + e.getMessage());
        }
    }


    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }
}