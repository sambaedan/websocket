package com.websockets.web_socket.consumer;

import com.websockets.web_socket.pojo.model.ChatMessagePojo;
import com.websockets.web_socket.service.ChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    private final ChatService chatService;  // creating helper class or seperate service would be much better

    public MessageListener(ChatService chatService) {
        this.chatService = chatService;
    }

    @RabbitListener(queues = "chatQueue")
    public void receiveMessage(ChatMessagePojo message) {
        System.out.println("Received message d: " + message.getContent());
        chatService.saveMessage(message);
    }
}
