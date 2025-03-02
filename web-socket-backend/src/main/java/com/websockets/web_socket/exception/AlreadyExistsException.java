package com.websockets.web_socket.exception;

import org.hibernate.service.spi.ServiceException;

public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String msg) {
        super(msg);
    }

}
