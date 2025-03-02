package com.websockets.web_socket.pojo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePojo implements Serializable {

    private String content;
    private String fileId;
    private String sender;
    private String receiver;
    private String fileUrl;
    private boolean saved;
    private LocalDateTime timestamp;
}
