package org.example.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse {
    private int status;
    private String message;
    public GenericResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}