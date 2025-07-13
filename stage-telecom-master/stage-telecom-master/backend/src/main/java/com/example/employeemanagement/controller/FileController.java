package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", defaultValue = "general") String category) {
        try {
            String fileName = fileStorageService.storeFile(file, category);
            
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("message", "File uploaded successfully");
            response.put("downloadUri", "/api/files/" + fileName);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not upload file: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName,
            HttpServletRequest request) {
        try {
            System.out.println("File download requested: " + fileName);

            // Load file as Resource
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            System.out.println("File found: " + resource.exists());
            System.out.println("File path: " + resource.getFile().getAbsolutePath());

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                // Fallback to default content type
                contentType = "application/octet-stream";
            }

            // If content type could not be determined, set default
            if (contentType == null) {
                if (fileName.toLowerCase().endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else {
                    contentType = "application/octet-stream";
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            System.out.println("Error loading file: " + fileName);
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/view/{fileName:.+}")
    public ResponseEntity<Resource> viewFile(
            @PathVariable String fileName, 
            HttpServletRequest request) {
        try {
            // Load file as Resource
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            // Determine content type for viewing
            String contentType = "application/pdf";
            if (fileName.toLowerCase().endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (fileName.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{fileName:.+}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        try {
            boolean deleted = fileStorageService.deleteFileByName(fileName);
            if (deleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "File deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "File not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not delete file: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/info/{fileName:.+}")
    public ResponseEntity<?> getFileInfo(@PathVariable String fileName) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);

            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("fileName", resource.getFilename());
            fileInfo.put("size", resource.getFile().length());
            fileInfo.put("lastModified", resource.getFile().lastModified());
            fileInfo.put("exists", resource.exists());
            fileInfo.put("readable", resource.isReadable());
            fileInfo.put("absolutePath", resource.getFile().getAbsolutePath());

            return ResponseEntity.ok(fileInfo);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Could not get file info: " + e.getMessage());
            errorResponse.put("fileName", fileName);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testFileAccess() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "File controller is working");
        response.put("uploadDir", "uploads");

        // List files in upload directories
        try {
            Path uploadsPath = Paths.get("uploads");
            if (Files.exists(uploadsPath)) {
                response.put("uploadsExists", true);
                response.put("uploadsPath", uploadsPath.toAbsolutePath().toString());

                // List subdirectories
                String[] subdirs = {"primary-education", "secondary-education", "higher-education"};
                Map<String, Object> subdirInfo = new HashMap<>();

                for (String subdir : subdirs) {
                    Path subdirPath = uploadsPath.resolve(subdir);
                    Map<String, Object> info = new HashMap<>();
                    info.put("exists", Files.exists(subdirPath));
                    if (Files.exists(subdirPath)) {
                        try {
                            info.put("fileCount", Files.list(subdirPath).count());
                        } catch (Exception e) {
                            info.put("fileCount", "Error counting files");
                        }
                    }
                    subdirInfo.put(subdir, info);
                }
                response.put("subdirectories", subdirInfo);
            } else {
                response.put("uploadsExists", false);
            }
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}

