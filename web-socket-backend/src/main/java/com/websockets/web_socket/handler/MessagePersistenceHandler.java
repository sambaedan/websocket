package com.websockets.web_socket.handler;

import com.websockets.web_socket.handler.helper.WebSocketHelper;
import com.websockets.web_socket.model.User;
import com.websockets.web_socket.pojo.model.ChatMessagePojo;
import com.websockets.web_socket.publisher.MessagePublisher;
import com.websockets.web_socket.service.UserServiceImpl;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
/**
 * Handles saving chat messages to persistent storage.
 *
 * @author Sandesh Khanal
 */
public class MessagePersistenceHandler {
    private final MessagePublisher publisher;
    private final UserServiceImpl userService;
    private final UserPresenceHandler presenceHandler;

    public MessagePersistenceHandler(MessagePublisher publisher,
                                    UserServiceImpl userService,
                                    UserPresenceHandler presenceHandler) {
        this.publisher = publisher;
        this.userService = userService;
        this.presenceHandler = presenceHandler;
    }
    /**
     * Handles saving a chat message to persistent storage.
     *
     * @param session The WebSocket session.
     * @param payload The save message payload (e.g., "save:receiver:content").
     * @throws IOException If an I/O error occurs while sending a response.
     */
    public void handleMessageSaving(WebSocketSession session, String payload) throws IOException {
        String[] parts = payload.split(":", 3);
        if (parts.length < 3) {
            WebSocketHelper.sendError(session, "Invalid save message format");
            return;
        }

        User sender = presenceHandler.getUser(session.getId());
        if (sender == null) {
            WebSocketHelper.sendError(session, "Not authenticated");
            return;
        }

        String receiverUsername = parts[1];
        String content = parts[2];

        userService.findByUsername(receiverUsername).ifPresent(receiver -> {
            ChatMessagePojo message = new ChatMessagePojo();
            message.setContent(content);
            message.setSender(sender.getUsername());
            message.setReceiver(receiver.getUsername());
            message.setTimestamp(LocalDateTime.now());
            message.setSaved(true);
            publisher.sendMessage(message);

            try {
                WebSocketHelper.sendSuccess(session, "Message saved successfully");
            } catch (IOException e) {
                System.err.println("Error sending success message: " + e.getMessage());
            }
        });
    }
}