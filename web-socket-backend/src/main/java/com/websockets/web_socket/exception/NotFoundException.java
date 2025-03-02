package com.websockets.web_socket.exception;

import org.hibernate.service.spi.ServiceException;

public class NotFoundException extends ServiceException {
    public NotFoundException(String msg) {
        super(msg);
    }
}