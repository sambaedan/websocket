package com.websockets.web_socket.handler;

import com.websockets.web_socket.model.User;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Manages active users and their WebSocket sessions.
 * Tracks which users are online and broadcasts updates to all clients.
 *
 * @author Sandesh Khanal
 */
@Getter
public class UserPresenceHandler {
    private final Map<String, WebSocketSession> clientSessions = new ConcurrentHashMap<>();
    private final Map<String, User> activeUsers = new ConcurrentHashMap<>();
    /**
     * Adds a new user session to the active sessions map.
     *
     * @param sessionId The ID of the WebSocket session.
     * @param session The WebSocket session.
     * @param user The authenticated user.
     */
    public void addUserSession(String sessionId, WebSocketSession session, User user) {
        clientSessions.put(sessionId, session);
        activeUsers.put(sessionId, user);
    }

    /**
     * Removes a user session from the active sessions map.
     *
     * @param sessionId The ID of the WebSocket session.
     */
    public void removeUserSession(String sessionId) {
        clientSessions.remove(sessionId);
        activeUsers.remove(sessionId);
    }

    /**
     * Retrieves the user associated with a session ID.
     *
     * @param sessionId The ID of the WebSocket session.
     * @return The associated user, or null if not found.
     */
    public User getUser(String sessionId) {
        return activeUsers.get(sessionId);
    }

    /**
     * Broadcasts the updated list of active users to all connected clients.
     */
    public void broadcastUserList() {
        StringBuilder userList = new StringBuilder("users:");
        activeUsers.values().stream()
                .map(User::getUsername)
                .distinct()
                .forEach(username -> userList.append(username).append(","));

        clientSessions.values().forEach(session -> {
            try {
                session.sendMessage(new TextMessage(userList.toString()));
            } catch (IOException e) {
                System.err.println("Error broadcasting user list: " + e.getMessage());
            }
        });
    }
}