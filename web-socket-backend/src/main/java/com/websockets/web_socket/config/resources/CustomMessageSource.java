package com.websockets.web_socket.config.resources;


import com.websockets.web_socket.config.resources.locale.LocaleThreadStorage;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class CustomMessageSource {

    private final MessageSource messageSource;

    public CustomMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String get(String code) {
        return messageSource.getMessage(code, null, LocaleThreadStorage.getLocale());
    }

    public String get(String code, Object... objects) {
        return messageSource.getMessage(code, objects, LocaleThreadStorage.getLocale());
    }


}
