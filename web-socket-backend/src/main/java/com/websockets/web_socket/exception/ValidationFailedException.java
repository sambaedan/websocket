package com.websockets.web_socket.exception;

import org.hibernate.service.spi.ServiceException;

public class ValidationFailedException extends ServiceException {
    public ValidationFailedException(String msg) {
        super(msg);
    }

}
