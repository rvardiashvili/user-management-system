package org.example.employeemanagement.dto.entities.requests;

import lombok.Data;

@Data
public class UnitRequest {
    private String name;
    private String description;
    private Long organisationId;
    private Long parentId;
}