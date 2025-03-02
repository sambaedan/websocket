package com.websockets.web_socket.pojo.util;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component                   //for controllers or api endpoint no need for websocket only
@Scope("prototype")
public class GlobalApiResponse implements Serializable {
    private boolean status;
    private String message;
    private Object data;
    private List<String> error;
}
