package org.example.employeemanagement.dto.people.requests;

import lombok.Data;
import java.util.Set;

@Data
public class AdminUserUpdateRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private String role;
    private Set<String> permissions;
}