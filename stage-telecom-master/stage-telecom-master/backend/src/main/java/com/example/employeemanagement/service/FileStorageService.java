package com.example.employeemanagement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file, String directory) throws IOException;
    byte[] loadFile(String filePath) throws IOException;
    Resource loadFileAsResource(String fileName) throws IOException;
    void deleteFile(String filePath) throws IOException;
    boolean deleteFileByName(String fileName);
    boolean fileExists(String filePath);
}

