package com.websockets.web_socket.service;

public interface FileStorageService {

    String save(String fileName,String mimeType, byte[] data);

}
