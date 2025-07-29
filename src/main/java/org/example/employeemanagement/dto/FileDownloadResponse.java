package org.example.employeemanagement.dto;

import org.springframework.core.io.InputStreamResource;

public record FileDownloadResponse(
        InputStreamResource resource,
        String fileName,
        String contentType,
        long fileSize
) {}