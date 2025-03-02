package com.websockets.web_socket.handler;

import com.websockets.web_socket.handler.helper.WebSocketHelper;
import com.websockets.web_socket.service.UserServiceImpl;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
/**
 * Handles user authentication via WebSocket.
 * Validates user credentials and manages session authentication.
 * @author Sandesh Khanal
 *ToDo integrate it with Spring security later.
 */
public class AuthenticationHandler {
    private final UserServiceImpl userService;
    private final UserPresenceHandler presenceHandler;

    public AuthenticationHandler(UserServiceImpl userService, UserPresenceHandler presenceHandler) {
        this.userService = userService;
        this.presenceHandler = presenceHandler;
    }
    /**
     * Handles the authentication process for a WebSocket session.
     *
     * @param session The WebSocket session.
     * @param payload The authentication payload (e.g., "auth:username").
     */
    public void handleAuthentication(WebSocketSession session, String payload) {
        String username = payload.substring("auth:".length());
        userService.findByUsername(username).ifPresentOrElse(
                user -> {
                    presenceHandler.addUserSession(session.getId(), session, user);
                    presenceHandler.broadcastUserList();
                    try {
                        WebSocketHelper.sendSuccess(session, "Authenticated as " + username);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> WebSocketHelper.sendError(session, "Invalid username")
        );
    }
}