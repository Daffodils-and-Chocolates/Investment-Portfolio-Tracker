package com.example.demo.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.json.JSONObject;

@Component
public class FinnhubWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JSONObject jsonResponse = new JSONObject(payload);

        // Handle ping messages
        if (jsonResponse.has("type") && "ping".equals(jsonResponse.getString("type"))) {
            // Respond with pong    
            session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
            return;
        }

        // Process stock data
        System.out.println("Received message:" + jsonResponse.toString());

        // Example: Extract the stock price and symbol
        if (jsonResponse.has("data")) {
            String symbol = jsonResponse.getJSONArray("data").getJSONObject(0).getString("s");
            double price = jsonResponse.getJSONArray("data").getJSONObject(0).getDouble("p");
            System.out.println("Stock: " + symbol + " | Price: " + price);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Handle any errors
        exception.printStackTrace();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Handle WebSocket connection closure
        System.out.println("WebSocket connection closed");
    }
}
