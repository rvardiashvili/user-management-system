package org.example.employeemanagement.dto.entities.requests;

import lombok.Data;

@Data
public class CompanyRequest {
    private String company_name;
    private String company_description;

    CompanyRequest() {}

}
