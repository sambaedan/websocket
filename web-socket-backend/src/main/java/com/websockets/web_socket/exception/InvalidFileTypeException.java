package com.websockets.web_socket.exception;

import org.hibernate.service.spi.ServiceException;

public class InvalidFileTypeException extends ServiceException {
    public InvalidFileTypeException(String message) {
        super(message);
    }
}