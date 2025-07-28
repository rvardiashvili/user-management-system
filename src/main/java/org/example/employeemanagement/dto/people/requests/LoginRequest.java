package org.example.employeemanagement.dto.people.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}