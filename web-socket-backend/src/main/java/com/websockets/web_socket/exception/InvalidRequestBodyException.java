package com.websockets.web_socket.exception;

import java.rmi.ServerException;

public class InvalidRequestBodyException extends ServerException {
    public InvalidRequestBodyException(String e) {
        super(e);
    }
}