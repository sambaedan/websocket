package com.websockets.web_socket.handler;

import com.websockets.web_socket.handler.helper.WebSocketHelper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
/**
 * Main WebSocket handler that delegates incoming messages to appropriate handlers.
 *
 * @author Sandesh Khanal
 */
public class WebSocketHandler extends TextWebSocketHandler {
    private final AuthenticationHandler authHandler;
    private final ChatMessageHandler chatHandler;
    private final FileUploadHandler fileHandler;
    private final MessagePersistenceHandler persistenceHandler;
    private final UserPresenceHandler presenceHandler;

    /**
     * Constructs a new WebSocketHandler.
     *
     * @param authHandler The handler for authentication.
     * @param chatHandler The handler for chat messages.
     * @param fileHandler The handler for file uploads.
     * @param persistenceHandler The handler for message persistence.
     * @param presenceHandler The handler for user presence.
     */
    public WebSocketHandler(AuthenticationHandler authHandler,
                            ChatMessageHandler chatHandler,
                            FileUploadHandler fileHandler,
                            MessagePersistenceHandler persistenceHandler,
                            UserPresenceHandler presenceHandler) {
        this.authHandler = authHandler;
        this.chatHandler = chatHandler;
        this.fileHandler = fileHandler;
        this.persistenceHandler = persistenceHandler;
        this.presenceHandler = presenceHandler;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Client connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (payload.startsWith("auth:")) {
            authHandler.handleAuthentication(session, payload);
        } else if (payload.startsWith("msg:")) {
            String[] parts = payload.split(":", 3);
            if (parts.length < 3) {
                WebSocketHelper.sendError(session, "Invalid message format");
                return;
            }
            chatHandler.handleChatMessage(session, parts[1], parts[2]);
        } else if (payload.startsWith("filemeta:")) {
            fileHandler.handleFileMetadata(session, payload);
        } else if (payload.startsWith("save:")) {
            persistenceHandler.handleMessageSaving(session, payload);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        fileHandler.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        presenceHandler.removeUserSession(session.getId());
        presenceHandler.broadcastUserList();
    }

    @Override
    public boolean supportsPartialMessages() {
        return true;
    }
}