package com.iskander.book_network.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorgeService {

    @Value("${spring.application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile fileSource,
                           @NonNull Long userId) {
        final String fileUploadSubPath = "users"+ File.separator + userId;
        return fileUpload(fileSource,fileUploadSubPath);
    }

    private String fileUpload(@NonNull MultipartFile fileSource,
                              @NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated =  targetFolder.mkdirs();
            if (!folderCreated) {
               log.warn("Failed to create folder {}", finalUploadPath);
               return null;
            }
        }
        final String fileExtension = getFileExtension(fileSource.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath,fileSource.getBytes());
            log.info("Saved file to {}", targetFilePath);
        } catch (IOException e) {
            log.error("File was not saved", e);
        }
        return targetFilePath;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return null;
        }
        return fileName.substring(lastIndex +1)
                .toLowerCase();
    }
}
