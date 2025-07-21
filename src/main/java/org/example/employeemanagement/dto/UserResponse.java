package org.example.employeemanagement.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
}