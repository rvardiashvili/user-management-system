package org.example.employeemanagement.dto;

import lombok.Data;
import java.util.Set;

@Data
public class AdminUserUpdateRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private Set<String> roles;
    private Set<String> permissions;
}