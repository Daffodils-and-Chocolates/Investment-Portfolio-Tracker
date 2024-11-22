package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FrontendWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(FrontendWebSocketHandler.class);

    // Use ConcurrentHashMap for thread-safe session tracking
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Store session with its ID as the key
        sessions.put(session.getId(), session);

        logger.info("Frontend WebSocket connection established");
        logger.info("Session ID: {}", session.getId());
        logger.info("Session Details:");
        logger.info("  - Local Address: {}", session.getLocalAddress());
        logger.info("  - Remote Address: {}", session.getRemoteAddress());
        logger.info("  - Protocol: {}", session.getAcceptedProtocol());
        logger.info("  - URI: {}", session.getUri());
        logger.info("Total active sessions: {}", sessions.size());

        // Log thread information
        Thread currentThread = Thread.currentThread();
        logger.info("Thread Information:");
        logger.info("  - Thread Name: {}", currentThread.getName());
        logger.info("  - Thread ID: {}", currentThread.getId());
        logger.info("  - Thread State: {}", currentThread.getState());

        TextMessage welcomeMessage = new TextMessage("WebSocket connection established successfully");
        session.sendMessage(welcomeMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Received from frontend (Session {}): {}",
                session.getId(), message.getPayload());

        // Optional: Echo back the message
        if (session.isOpen()) {
            session.sendMessage(new TextMessage("Received: " + message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the specific session
        if (session.isOpen()) {
            sessions.put(session.getId(), session); // Re-add session if needed
        }

        logger.info("Frontend WebSocket connection closed");
        logger.info("Session ID: {}", session.getId());
        logger.info("Close Status: {}", status);
        logger.info("Remaining active sessions: {}", sessions.size());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Transport error in WebSocket session {}: {}",
                session.getId(), exception.getMessage(), exception);

        // Log detailed error information
        logger.error("Error Details:", exception);

        // Attempt to close the session if it's still open
        if (session.isOpen()) {
            session.close();
        }

        // Remove the session
        sessions.remove(session.getId());
    }

    public void sendMessageToFrontend(List<String> messages) {
        // Log extensive debugging information
        logger.info("=== sendMessageToFrontend DEBUG START ===");
        logger.info("Thread Information:");
        Thread currentThread = Thread.currentThread();
        logger.info("  - Thread Name: {}", currentThread.getName());
        logger.info("  - Thread ID: {}", currentThread.getId());
        logger.info("  - Thread State: {}", currentThread.getState());

        logger.info("Attempting to send messages: {}", messages);
        logger.info("Current active sessions (before sending): {}", sessions.size());

        // Log all session details
        sessions.forEach((id, session) -> {
            logger.info("Session Details:");
            logger.info("  - ID: {}", id);
            logger.info("  - Is Open: {}", session.isOpen());
            logger.info("  - Local Address: {}", session.getLocalAddress());
            logger.info("  - Remote Address: {}", session.getRemoteAddress());
        });

        if (sessions.isEmpty()) {
            logger.warn("No active WebSocket sessions available.");
            logger.info("=== sendMessageToFrontend DEBUG END ===");
            return;
        }

        // Create a copy of sessions to avoid concurrent modification
        Map<String, WebSocketSession> activeSessions = new HashMap<>(sessions);

        for (Map.Entry<String, WebSocketSession> entry : activeSessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            try {
                if (session.isOpen()) {
                    for (String message : messages) {
                        logger.info("Sending message to session {}: {}",
                                session.getId(), message);
                        session.sendMessage(new TextMessage(message));
                    }
                } else {
                    logger.warn("Session is not open: {}", session.getId());
                    sessions.remove(entry.getKey());
                }
            } catch (IOException e) {
                logger.error("Error sending message to session {}: {}",
                        session.getId(), e.getMessage(), e);
                sessions.remove(entry.getKey());
            }
        }

        logger.info("Current active sessions (after sending): {}", sessions.size());
        logger.info("=== sendMessageToFrontend DEBUG END ===");
    }

    // Add a method to manually check and log session status
    public void logSessionStatus() {
        logger.info("=== Manual Session Status Check ===");
        logger.info("Total Sessions: {}", sessions.size());
        sessions.forEach((id, session) -> {
            logger.info("Session ID: {}", id);
            logger.info("  - Is Open: {}", session.isOpen());
            logger.info("  - Local Address: {}", session.getLocalAddress());
            logger.info("  - Remote Address: {}", session.getRemoteAddress());
        });
    }
}