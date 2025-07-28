package org.example.employeemanagement.dto.entities.requests;

import lombok.Data;

@Data
public class OrganisationRequest {
    private String name;
    private String description;
    private Long organisationId;
    private Long companyId;
}