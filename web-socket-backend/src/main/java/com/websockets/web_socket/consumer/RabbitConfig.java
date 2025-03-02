package com.websockets.web_socket.consumer;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue chatQueue() {
        return new Queue("chatQueue", true);
    }
}
