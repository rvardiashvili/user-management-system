package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.Permission;

import java.util.Set;

@Data
public class PermissionResponse {
    private Long userId;
    private String email;
    private Set<Permission> permissions;


}