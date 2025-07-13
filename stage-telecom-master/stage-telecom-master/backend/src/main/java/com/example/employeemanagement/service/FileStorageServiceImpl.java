package com.example.employeemanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String directory) throws IOException {
        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir, directory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Store file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename; // Return just the filename for easier access
    }

    @Override
    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws IOException {
        try {
            // First try to find the file in the main upload directory
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            }
            
            // If not found, search in subdirectories
            String[] subdirectories = {"primary-education", "secondary-education", "higher-education", "general"};
            for (String subdir : subdirectories) {
                filePath = this.fileStorageLocation.resolve(subdir).resolve(fileName).normalize();
                resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    return resource;
                }
            }
            
            throw new IOException("File not found: " + fileName);
        } catch (MalformedURLException ex) {
            throw new IOException("File not found: " + fileName, ex);
        }
    }

    @Override
    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
    }

    @Override
    public boolean deleteFileByName(String fileName) {
        try {
            // First try to find and delete the file in the main upload directory
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
            
            // If not found, search in subdirectories
            String[] subdirectories = {"primary-education", "secondary-education", "higher-education", "general"};
            for (String subdir : subdirectories) {
                filePath = this.fileStorageLocation.resolve(subdir).resolve(fileName).normalize();
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    return true;
                }
            }
            
            return false; // File not found
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
}

