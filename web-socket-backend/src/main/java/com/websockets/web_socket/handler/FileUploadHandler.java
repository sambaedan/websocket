package com.websockets.web_socket.handler;

import com.websockets.web_socket.handler.helper.WebSocketHelper;
import com.websockets.web_socket.model.FileMetadata;
import com.websockets.web_socket.service.FileStorageService;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Handles file uploads via WebSocket, including metadata and binary data.
 *
 * @author Sandesh Khanal
 */
public class FileUploadHandler {
    private final Map<String, FileMetadata> pendingFiles = new ConcurrentHashMap<>();
    private final Map<String, ByteBuffer> binaryBuffers = new ConcurrentHashMap<>();
    private final FileStorageService fileStorageService;
    private final ChatMessageHandler chatMessageHandler;
    private final UserPresenceHandler presenceHandler;

    public FileUploadHandler(FileStorageService fileStorageService,
                             ChatMessageHandler chatMessageHandler,
                             UserPresenceHandler presenceHandler) {
        this.fileStorageService = fileStorageService;
        this.chatMessageHandler = chatMessageHandler;
        this.presenceHandler = presenceHandler;
    }
    /**
     * Handles file metadata received from a WebSocket session.
     *
     * @param session The WebSocket session.
     * @param payload The metadata payload (e.g., "filemeta:receiver:filename:mimetype").
     */
    public void handleFileMetadata(WebSocketSession session, String payload) {
        String[] parts = payload.split(":", 4);
        if (parts.length < 4) {
            WebSocketHelper.sendError(session, "Invalid file metadata");
            return;
        }

        String sessionId = session.getId();
        pendingFiles.put(sessionId, new FileMetadata(
                parts[1],  // receiverUsername
                parts[2],  // fileName
                parts[3]   // mimeType
        ));
    }

    /**
     * Handles binary file data received from a WebSocket session.
     *
     * @param session The WebSocket session.
     * @param message The binary message containing file data.
     */
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        boolean isLast = message.isLast();
        String sessionId = session.getId();
        ByteBuffer buffer = message.getPayload();

        ByteBuffer accumulatedBuffer = binaryBuffers.computeIfAbsent(
                sessionId, k -> ByteBuffer.allocate(0));
        accumulatedBuffer = concatBuffers(accumulatedBuffer, buffer);
        binaryBuffers.put(sessionId, accumulatedBuffer);

        if (isLast) {
            processCompleteBinaryMessage(session, accumulatedBuffer);
            binaryBuffers.remove(sessionId);
        }
    }/**
     * Concatenates two ByteBuffers into one.
     *
     * @param a The first ByteBuffer.
     * @param b The second ByteBuffer.
     * @return The concatenated ByteBuffer.
     */
    private ByteBuffer concatBuffers(ByteBuffer a, ByteBuffer b) {
        ByteBuffer combined = ByteBuffer.allocate(a.remaining() + b.remaining());
        combined.put(a);
        combined.put(b);
        combined.flip();
        return combined;
    }
    /**
     * Processes a complete binary message and stores the file.
     *
     * @param session The WebSocket session.
     * @param buffer The complete binary data.
     */
    private void processCompleteBinaryMessage(WebSocketSession session, ByteBuffer buffer) {
        String sessionId = session.getId();
        FileMetadata metadata = pendingFiles.get(sessionId);

        if (metadata == null) {
            WebSocketHelper.sendError(session, "File metadata missing");
            return;
        }

        try {
            byte[] fileData = new byte[buffer.remaining()];
            buffer.get(fileData);

            String fileUrl = fileStorageService.save(metadata.getFileName(),metadata.getMimeType(), fileData);
            chatMessageHandler.sendChatMessage(
                    presenceHandler.getUser(sessionId),
                    metadata.getUsername(),
                    null,
                    fileUrl,
                    false
            );
            pendingFiles.remove(sessionId);
        } catch (Exception e) {
            WebSocketHelper.sendError(session, "File upload failed: " + e.getMessage());
        }
    }
}