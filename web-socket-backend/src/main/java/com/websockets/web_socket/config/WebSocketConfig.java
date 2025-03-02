package com.websockets.web_socket.config;

import com.websockets.web_socket.handler.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import static jakarta.websocket.ContainerProvider.getWebSocketContainer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final AuthenticationHandler authHandler;
    private final ChatMessageHandler chatHandler;
    private final FileUploadHandler fileHandler;
    private final UserPresenceHandler presenceHandler;
    private final MessagePersistenceHandler persistenceHandler;

    public WebSocketConfig(AuthenticationHandler authHandler, ChatMessageHandler chatHandler, FileUploadHandler fileHandler, UserPresenceHandler userPresenceHandler, UserPresenceHandler presenceHandler, MessagePersistenceHandler persistenceHandler) {
        this.authHandler = authHandler;
        this.chatHandler = chatHandler;
        this.fileHandler = fileHandler;
        this.presenceHandler = presenceHandler;
        this.persistenceHandler = persistenceHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(
                        authHandler,
                        chatHandler,
                        fileHandler,
                        persistenceHandler,
                        presenceHandler
                ), "/chat")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new DefaultHandshakeHandler(
                        new TomcatRequestUpgradeStrategy() {
                            {
                                getWebSocketContainer().setDefaultMaxBinaryMessageBufferSize(1024 * 1024); // 1MB
                            }
                        }));
    }
}