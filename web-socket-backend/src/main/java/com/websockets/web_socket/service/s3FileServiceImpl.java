package com.websockets.web_socket.service;

import org.springframework.stereotype.Service;

@Service
public class s3FileServiceImpl implements FileStorageService {
    @Override
    public String save(String fileName,String mimeType, byte[] data) {
        return "";
    }

    //implementation for file save in aws s3
}
