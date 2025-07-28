package org.example.employeemanagement.dto.entities.responses;

import lombok.Data;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.dto.people.UserDetails;

@Data
public class CompanyResponse {
    private Long company_id;
    private String company_name;
    private String company_description;
    private UserDetails company_creator;

    public CompanyResponse(Company company) {
        company_id = company.getCompanyId();
        company_name = company.getCompanyName();
        company_description = company.getCompanyDescription();
        company_creator = new UserDetails(company.getCreator().getUser());
    }
}
