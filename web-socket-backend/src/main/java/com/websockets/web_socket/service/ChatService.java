package com.websockets.web_socket.service;

import com.websockets.web_socket.pojo.model.ChatMessagePojo;

public interface ChatService {

    void saveMessage(ChatMessagePojo message);

}
