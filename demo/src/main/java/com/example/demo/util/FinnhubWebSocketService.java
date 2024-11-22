//import org.springframework.web.socket.TextMessage;session.sendMessage(new TextMessage("{\"type\":\"subscribe\",\"symbol\":\"AAPL\"}"));
//        session.sendMessage(new TextMessage("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}"));
//        session.sendMessage(new TextMessage("{\"type\":\"subscribe\",\"symbol\":\"IC MARKETS:1\"}"));
package com.example.demo.util;

import com.example.demo.config.FrontendWebSocketHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FinnhubWebSocketService {

    private static final String FINNHUB_WS_URL = "wss://ws.finnhub.io?token=csomuhpr01qt3r34doe0csomuhpr01qt3r34doeg";
    private WebSocketClient webSocketClient;
    private WebSocketSession session;
    private final FrontendWebSocketHandler frontendWebSocketHandler;
    List<String> symbols = Arrays.asList(
            "AAPL",
//            "BINANCE:BTCUSDT",
            "IC MARKETS:1"
    );

    public FinnhubWebSocketService(FrontendWebSocketHandler frontendWebSocketHandler) {
        this.webSocketClient = new StandardWebSocketClient();
        this.frontendWebSocketHandler = frontendWebSocketHandler;
    }

    public void connectToFinnhub() {
        try {
            // Create the WebSocketHandler to handle messages
            WebSocketHandler webSocketHandler = new TextWebSocketHandler() {
                @Override
                public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                    // Log all messages
                    System.out.println("Received message: " + message.getPayload());

                    // Parse the incoming JSON response
                    JSONObject jsonResponse = new JSONObject(message.getPayload());

                    // Check for the "ping" type and respond with "pong"
                    if (jsonResponse.has("type") && "ping".equals(jsonResponse.getString("type"))) {
                        // Respond with pong to keep the connection alive
                        session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
                        return;
                    }

                    // Handle trade data (if present)
                    List<String> stringList = new ArrayList<>();

                    if ("trade".equals(jsonResponse.getString("type")) && jsonResponse.has("data")) {
                        JSONArray data = jsonResponse.getJSONArray("data");

                        String response ="";

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject stockData = data.getJSONObject(i);

                            // Extract stock symbol, price, timestamp, and volume
                            String symbol = stockData.getString("s"); // Stock symbol (e.g., "AAPL")
                            double price = stockData.getDouble("p");  // Stock price
                            long timestamp = stockData.getLong("t"); // Trade timestamp
                            double volume = stockData.getDouble("v"); // Trade volume

                            // Print/log the stock information
                            System.out.println("Stock: " + symbol + " | Price: " + price + " | Timestamp: " + timestamp + " | Volume: " + volume);

                            response = "Stock: " + symbol + " | Price: " + price + " | Timestamp: " + timestamp + " | Volume: " + volume;
                            stringList.add(response);
                        }
                    }

                    frontendWebSocketHandler.sendMessageToFrontend(stringList);
                    System.out.println("Data sent to frontend: " + stringList);

                }

                @Override
                public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                    // Connection established, subscribe to stock symbols
                    System.out.println("Connected to Finnhub WebSocket");

                    // Store session for later use (if necessary for unsubscribing)
                    FinnhubWebSocketService.this.session = session;

                    // Send subscription messages for each symbol in the watchlist
                   for (String symbol : symbols) {
                       session.sendMessage(new TextMessage("{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}"));
                   }
                }

                @Override
                public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                    // Handle any WebSocket transport errors
                    exception.printStackTrace();
                }

                @Override
                public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                    // WebSocket connection closed
                    System.out.println("WebSocket connection closed");
                }
            };

            // Connect to Finnhub WebSocket
            webSocketClient.doHandshake(webSocketHandler, FINNHUB_WS_URL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to unsubscribe from a stock symbol
    public void unsubscribe(String symbol) {
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage("{\"type\":\"unsubscribe\",\"symbol\":\"" + symbol + "\"}"));
                System.out.println("Unsubscribed from symbol: " + symbol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}