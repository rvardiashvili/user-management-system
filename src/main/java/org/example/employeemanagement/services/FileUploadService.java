package org.example.employeemanagement.services;

import io.minio.*;
import io.minio.errors.MinioException;

import org.example.employeemanagement.domain.UploadedFile;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.FileDataResponse;
import org.example.employeemanagement.dto.FileDownloadResponse;
import org.example.employeemanagement.repositories.people.UploadRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    private final MinioClient minioClient;
    private final UploadRepository uploadRepository;
    private final UserRepository userRepository;

    @Value("${minio.bucket.name}")
    private String defaultBucketName;

    public FileUploadService(MinioClient minioClient, UploadRepository uploadRepository, UserRepository userRepository) {
        this.minioClient = minioClient;
        this.uploadRepository = uploadRepository;
        this.userRepository = userRepository;
    }

    public String uploadFile(MultipartFile file, String email) {

        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(defaultBucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(defaultBucketName).build());
                System.out.println("Bucket '" + defaultBucketName + "' created successfully.");
            } else {
                System.out.println("Bucket '" + defaultBucketName + "' already exists.");
            }

            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(defaultBucketName)
                                .object(objectName)
                                .stream(is, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setObjectName(objectName);
            uploadedFile.setFileName(file.getOriginalFilename());
            uploadedFile.setFileSize(file.getSize());
            uploadedFile.setUploadedDate(LocalDateTime.now());
            uploadedFile.setFileType(file.getContentType());
            uploadedFile.setBucketName(defaultBucketName);
            uploadedFile.setUser(userRepository.findByEmail(email).orElseThrow());
            uploadRepository.save(uploadedFile);
            System.out.println("File uploaded successfully as object '" + objectName + "' to bucket '" + defaultBucketName + "'.");
            return objectName;

        } catch (MinioException e) {
            System.err.println("MinIO error during upload: " + e.getMessage());
            System.err.println("HTTP trace: " + e.httpTrace());
            return e.getMessage();
        } catch (IOException e) {
            System.err.println("File I/O error during upload: " + e.getMessage());
            return e.getMessage();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Security/Algorithm error during upload: " + e.getMessage());
            return e.getMessage();
        }
    }

    public List<FileDataResponse> getAllFiles(String email) {
        User user =  userRepository.findByEmail(email).orElseThrow();
        return uploadRepository.findByUser(user).stream().map(fileData -> new FileDataResponse(fileData.getFileName(), fileData.getFileType(), fileData.getFileSize(), fileData.getId())).toList();
    }

    public FileDataResponse getFile(Long id, String email) {
        User user =  userRepository.findByEmail(email).orElseThrow();
        UploadedFile uploadedFile = uploadRepository.findById(id).orElseThrow();
        if (uploadedFile.getUser().getEmail().equals(user.getEmail())) {
            return new FileDataResponse(uploadedFile.getFileName(), uploadedFile.getFileType(), uploadedFile.getFileSize(), uploadedFile.getId());
        }
        return null;
    }

    public void deleteFile(Long id, String email) {
        User user =  userRepository.findByEmail(email).orElseThrow();
        UploadedFile uploadedFile = uploadRepository.findById(id).orElseThrow();

        if (uploadedFile.getUser().getEmail().equals(user.getEmail())) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(defaultBucketName).object(uploadedFile.getObjectName()).build()
                );
                uploadRepository.deleteById(id);
            }catch (Exception e) {
                System.err.println("Error while removing file: " + e.getMessage());
            }
        }
    }

    public FileDownloadResponse downloadFile(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " not found"));
        UploadedFile uploadedFile = uploadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File with ID " + id + " not found"));

        if (!uploadedFile.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: File does not belong to user " + email);
        }

        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucketName)
                            .object(uploadedFile.getObjectName())
                            .build());

            return new FileDownloadResponse(
                    new InputStreamResource(stream),
                    uploadedFile.getFileName(),
                    uploadedFile.getFileType(),
                    uploadedFile.getFileSize()
            );

        } catch (MinioException e) {
            System.err.println("MinIO error during download of object '" + uploadedFile.getObjectName() + "': " + e.getMessage());
            System.err.println("HTTP trace: " + e.httpTrace());

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "MinIO download failed: " + e.getMessage(), e);
        } catch (IOException e) {
            System.err.println("File I/O error during download of object '" + uploadedFile.getObjectName() + "': " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File I/O error during download: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Security/Algorithm error during download of object '" + uploadedFile.getObjectName() + "': " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Security/Algorithm error during download: " + e.getMessage(), e);
        }
    }


}