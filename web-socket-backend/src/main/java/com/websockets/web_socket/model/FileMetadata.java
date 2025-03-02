package com.websockets.web_socket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileMetadata {
    private final String username;
    private final String fileName;
    private final String mimeType;

}