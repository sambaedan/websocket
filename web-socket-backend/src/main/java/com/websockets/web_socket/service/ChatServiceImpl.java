package com.websockets.web_socket.service;

import com.websockets.web_socket.model.ChatMessage;
import com.websockets.web_socket.pojo.model.ChatMessagePojo;
import com.websockets.web_socket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service implementation for handling chat messages.
 * <p>
 * This service provides functionality to save, retrieve, and manage chat messages.
 * <p>
 * The choice of storage method depends on the scale of the application.
 * For small, single-instance applications, an in-memory store like
 * {@link ConcurrentHashMap} can be sufficient.
 * However, in distributed or microservices environments,
 * a more robust solution like Redis is recommended to provide scalability, high availability, and persistence.
 * <p>
 * A Strategy Pattern can also be used to choose between different storage methods at runtime. This allows the system to switch between a simple in-memory cache and a more complex, distributed cache (Redis) depending on the deployment environment.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMessageRepository repository;
    private final ConcurrentHashMap<String, ChatMessage> unsavedMessages = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long MESSAGE_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5); // 5 minutes

    @Override
    public void saveMessage(ChatMessagePojo message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(message.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setSaved(message.isSaved());
        chatMessage.setReceiver(message.getReceiver());
        chatMessage.setSender(message.getSender());
        chatMessage.setFileUrl(message.getFileUrl());

        if (message.isSaved()) {
            repository.save(chatMessage);
        } else {
            String messageId = generateMessageId(message);
            unsavedMessages.put(messageId, chatMessage);
            scheduleMessageExpiration(messageId);
        }
    }

    private void scheduleMessageExpiration(String messageId) {
        scheduler.schedule(() -> {
            ChatMessage expiredMessage = unsavedMessages.remove(messageId);
            if (expiredMessage != null) {
                System.out.println("Message expired and removed: " + expiredMessage.getContent());
            }
        }, MESSAGE_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    private String generateMessageId(ChatMessagePojo message) {
        return message.getSender() + ":" + message.getReceiver() + ":" + message.getTimestamp().toString();
    }
}
