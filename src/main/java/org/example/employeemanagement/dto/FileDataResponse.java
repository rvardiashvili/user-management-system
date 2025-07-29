package org.example.employeemanagement.dto;

import org.springframework.core.io.InputStreamResource;

public record FileDataResponse(
        String fileName,
        String contentType,
        long fileSize,
        long fileId
) {}