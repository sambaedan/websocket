package com.websockets.web_socket.handler;

import com.websockets.web_socket.handler.helper.WebSocketHelper;
import com.websockets.web_socket.model.User;
import com.websockets.web_socket.pojo.model.ChatMessagePojo;
import com.websockets.web_socket.publisher.MessagePublisher;
import com.websockets.web_socket.service.UserServiceImpl;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
/**
 * Handles sending and processing of chat messages via WebSocket.
 * Manages both text and file-based messages.
 *
 * @author Sandesh Khanal
 */
public class ChatMessageHandler {
    private final MessagePublisher publisher;
    private final UserPresenceHandler presenceHandler;
    private final UserServiceImpl userService;

    public ChatMessageHandler(MessagePublisher publisher,
                              UserPresenceHandler presenceHandler,
                              UserServiceImpl userService) {
        this.publisher = publisher;
        this.presenceHandler = presenceHandler;
        this.userService = userService;
    }
    /**
     * Handles incoming chat messages from a WebSocket session.
     *
     * @param session The WebSocket session.
     * @param receiverUsername The username of the message receiver.
     * @param content The content of the message.
     */
    public void handleChatMessage(WebSocketSession session, String receiverUsername, String content) {
        User sender = presenceHandler.getUser(session.getId());
        if (sender == null) {
            WebSocketHelper.sendError(session, "Not authenticated");
            return;
        }

        sendChatMessage(sender, receiverUsername, content, null, false);
        if (!sender.getUsername().equals(receiverUsername)) {
            sendChatMessage(sender, sender.getUsername(), content, null, false);
        }
    }
    /**
     * Sends a chat message to the specified receiver.
     *
     * @param sender The user sending the message.
     * @param receiverUsername The username of the receiver.
     * @param content The content of the message.
     * @param fileUrl The URL of the file (if any).
     * @param saved Whether the message is saved which is always false here we have
     *              separate handler for saving message MessagePersistenceHandler.
     *              so basically it is saved in concurrentHashMap and removed after
     *              5 minutes.
     */
    void sendChatMessage(User sender, String receiverUsername,
                         String content, String fileUrl, boolean saved) {
        userService.findByUsername(receiverUsername).ifPresent(receiver -> {
            ChatMessagePojo message = new ChatMessagePojo();
            message.setContent(content);
            message.setFileUrl(fileUrl);
            message.setSender(sender.getUsername());
            message.setReceiver(receiver.getUsername());
            message.setSaved(saved);
            message.setTimestamp(LocalDateTime.now());

            publisher.sendMessage(message);

            presenceHandler.getClientSessions().entrySet().stream()
                    .filter(entry -> receiver.getId().equals(
                            presenceHandler.getActiveUsers().get(entry.getKey()).getId()))
                    .findFirst()
                    .ifPresent(entry -> {
                        try {
                            String msgContent = fileUrl != null ?
                                    "File: " + fileUrl : content;
                            String formattedMessage = sender.getUsername().equals(receiverUsername) ?
                                    "You: " + msgContent : "From " + sender.getUsername() + ": " + msgContent;
                            entry.getValue().sendMessage(new TextMessage(formattedMessage));
                        } catch (IOException e) {
                            System.err.println("Error sending chat message: " + e.getMessage());  //use custom message and constants for maintainability.
                        }
                    });
        });
    }
}