
package com.example.realtimechat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> clients =
            ConcurrentHashMap.newKeySet();

    private final ConcurrentHashMap<WebSocketSession, String> users =
            new ConcurrentHashMap<>();

    private final ChatMessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(
            ChatMessageRepository messageRepository,
            ObjectMapper objectMapper) {
        this.messageRepository = messageRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        clients.add(session);
    }

    @Override
    protected void handleTextMessage(
            WebSocketSession session, TextMessage message) {

        String text = message.getPayload();

        if (text.startsWith("JOIN:")) {
            String name = text.substring(5).trim();

            if (name.isEmpty() || name.length() > 100) {
                return;
            }

            users.put(session, name);

            broadcast("SYSTEM:" + name + " joined the chat.");
            broadcastUsers();

            sendHistory(session);
            return;
        }

        if (text.startsWith("MSG:")) {
            String content = text.substring(4);
            int separator = content.indexOf(':');

            if (separator < 1) {
                return;
            }

            String sender = users.get(session);

            if (sender == null) {
                return;
            }

            String body = content.substring(separator + 1).trim();

            if (body.isEmpty() || body.length() > 2000) {
                return;
            }

            try {
                ChatMessage saved = messageRepository.save(
                        new ChatMessage(sender, body));

                broadcast("CHAT:" +
                        objectMapper.writeValueAsString(saved));

            } catch (Exception e) {
                System.err.println(
                        "Could not save or send message: " + e.getMessage());
                sendToSession(session,
                        "SYSTEM:Message could not be saved. Please try again.");
            }
        }
    }

    private void sendHistory(WebSocketSession session) {
        try {
            ArrayList<ChatMessage> history = new ArrayList<>(
                    messageRepository.findTop100ByOrderBySentAtDesc());

            Collections.reverse(history);

            String json = objectMapper.writeValueAsString(history);
            sendToSession(session, "HISTORY:" + json);

        } catch (Exception e) {
            System.err.println(
                    "Could not load chat history: " + e.getMessage());
        }
    }

    private void broadcastUsers() {
        String names = users.values().stream()
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.joining(","));

        broadcast("USERS:" + names);
    }

    private void sendToSession(WebSocketSession session, String text) {
        if (session.isOpen()) {
            try {
                synchronized (session) {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(text));
                    }
                }
            } catch (IOException e) {
                System.err.println(
                        "Message delivery failed: " + e.getMessage());
            }
        }
    }

    private void broadcast(String text) {
        for (WebSocketSession client : clients) {
            sendToSession(client, text);
        }
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session, CloseStatus status) {

        clients.remove(session);
        String name = users.remove(session);

        if (name != null) {
            broadcast("SYSTEM:" + name + " left the chat.");
            broadcastUsers();
        }
    }
}
