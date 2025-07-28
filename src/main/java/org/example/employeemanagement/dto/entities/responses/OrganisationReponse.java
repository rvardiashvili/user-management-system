package org.example.employeemanagement.dto.entities.responses;

import lombok.Data;
import org.example.employeemanagement.domain.entities.Organisation;

@Data
public class OrganisationReponse {
    private Long id;
    private String name;
    private String description;
    private CompanyResponse company;

    public OrganisationReponse(Organisation org) {
        this.id = org.getOrganisationId();
        this.name = org.getOrganisationName();
        this.description = org.getOrganisationDescription();
        this.company = new CompanyResponse(org.getCompany());
    }
}
