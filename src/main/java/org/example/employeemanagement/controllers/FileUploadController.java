package org.example.employeemanagement.controllers;

import org.example.employeemanagement.dto.FileDataResponse;
import org.example.employeemanagement.dto.FileDownloadResponse;
import org.example.employeemanagement.services.FileUploadService;
import org.example.employeemanagement.domain.UploadedFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/files/")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, Authentication authentication) {
        System.out.println("Uploading file " + file.getOriginalFilename());
        try {
            String objectName = fileUploadService.uploadFile(file, authentication.getName());
            return ResponseEntity.ok("File uploaded successfully! Object Name: " + objectName);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<FileDataResponse>> getAllFiles(Authentication authentication) {
        try {
            List<FileDataResponse> files = fileUploadService.getAllFiles(authentication.getName());
            return ResponseEntity.ok(files);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDataResponse> getFile(@PathVariable Long id, Authentication authentication) {
        try {
            FileDataResponse file = fileUploadService.getFile(id, authentication.getName());
            return ResponseEntity.ok(file);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id, Authentication authentication) {
        System.out.println("Attempting to download file with ID: " + id);
        try {
            FileDownloadResponse downloadResponse = fileUploadService.downloadFile(id, authentication.getName());

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadResponse.fileName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, downloadResponse.contentType());
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(downloadResponse.fileSize()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(downloadResponse.resource());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, Authentication authentication) {
        try {

            fileUploadService.deleteFile(id, authentication.getName());
            return ResponseEntity.ok("File with ID '" + id + "' deleted successfully.");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File deletion failed: " + e.getMessage());
        }
    }
}