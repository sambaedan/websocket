package com.websockets.web_socket.handler.helper;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import java.io.IOException;
/**
 * Utility class for sending WebSocket messages, including success and error responses.
 *
 * @author Sandesh Khanal
 */
public class WebSocketHelper {
    /**
     * Sends a success message to the client.
     *
     * @param session The WebSocket session.
     * @param message The success message to send.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public static void sendSuccess(WebSocketSession session, String message) throws IOException {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage("success:" + message));
        }
    }
    /**
     * Sends an error message to the client.
     *
     * @param session The WebSocket session.
     * @param error The error message to send.
     */
    public static void sendError(WebSocketSession session, String error) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("error:" + error));
            } catch (IOException e) {
                System.err.println("Error sending error message: " + e.getMessage());
            }
        }
    }
}