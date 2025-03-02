package com.websockets.web_socket.config;

import com.websockets.web_socket.handler.*;
import com.websockets.web_socket.publisher.MessagePublisher;
import com.websockets.web_socket.service.FileStorageService;
import com.websockets.web_socket.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomWebSocketConfig {

    @Bean
    public UserPresenceHandler userPresenceHandler() {
        return new UserPresenceHandler();
    }

    @Bean
    public AuthenticationHandler authenticationHandler(UserServiceImpl userService,
                                                       UserPresenceHandler presenceHandler) {
        return new AuthenticationHandler(userService, presenceHandler);
    }

    @Bean
    public ChatMessageHandler chatMessageHandler(MessagePublisher messagePublisher,
                                                 UserPresenceHandler presenceHandler,
                                                 UserServiceImpl userService) {
        return new ChatMessageHandler(messagePublisher, presenceHandler, userService);
    }

    @Bean
    public FileUploadHandler fileUploadHandler(@Qualifier("localFileStorageServiceImpl") FileStorageService fileStorageService,
                                               ChatMessageHandler chatMessageHandler,
                                               UserPresenceHandler presenceHandler) {
        return new FileUploadHandler(fileStorageService, chatMessageHandler, presenceHandler);
    }

    @Bean
    public MessagePersistenceHandler messagePersistenceHandler(MessagePublisher messagePublisher,
                                                               UserServiceImpl userService,
                                                               UserPresenceHandler presenceHandler) {
        return new MessagePersistenceHandler(messagePublisher, userService, presenceHandler);
    }

    @Bean
    public WebSocketHandler webSocketHandler(AuthenticationHandler authHandler,
                                             ChatMessageHandler chatHandler,
                                             FileUploadHandler fileHandler,
                                             MessagePersistenceHandler persistenceHandler,
                                             UserPresenceHandler presenceHandler) {
        return new WebSocketHandler(
                authHandler,
                chatHandler,
                fileHandler,
                persistenceHandler,
                presenceHandler
        );
    }
}
