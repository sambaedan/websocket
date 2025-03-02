package com.websockets.web_socket.exception;

import org.springframework.security.core.AuthenticationException;

public class SessionException extends AuthenticationException {

    public SessionException(String message) {
        super(message);
    }

}
