package com.websockets.web_socket.publisher;

import com.websockets.web_socket.pojo.model.ChatMessagePojo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public MessagePublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(ChatMessagePojo message) {
        amqpTemplate.convertAndSend("chatQueue", message);
    }
}
