package org.example.employeemanagement.dto;

import lombok.Data;

import java.util.Set;

@Data
public class GroupRequest {
    private String name;
    private Set<String> permissions;
}
