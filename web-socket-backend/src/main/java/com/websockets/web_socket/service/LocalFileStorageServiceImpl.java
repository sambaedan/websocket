package com.websockets.web_socket.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private static final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/uploads/";
    private final List<String> allowedMimeTypes = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "application/pdf"
    );
    private static final long MAX_FILE_SIZE = 1024 * 1024;
    @Override
    public String save(String fileName,String mimeType, byte[] data) {
        validateMimeType(mimeType);
        if (data.length > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds the limit of 10 MB");
        }

        File directory = new File(STORAGE_DIRECTORY);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directory: " + STORAGE_DIRECTORY);
            return null;
        }

        String uniqueFileName = UUID.randomUUID() + "-" + fileName;
        Path filePath = Paths.get(STORAGE_DIRECTORY, uniqueFileName);

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(data);
            fos.flush();
            return "http://localhost:8080/uploads/" + uniqueFileName; // dont hardcode it like this use config files
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void validateMimeType(String mimeType) {
        if (!allowedMimeTypes.contains(mimeType)) {
            throw new RuntimeException("Unsupported file type: " + mimeType);
        }
    }
}
