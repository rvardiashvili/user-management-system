package org.example.employeemanagement.dto.entities.responses;

import lombok.Data;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;

@Data
public class UnitResponse {
    private Long id;
    private String name;
    private String description;
    private OrganisationReponse organisation;

    public UnitResponse(OrganisationalUnit unit) {
        if(unit != null) {
        this.id = unit.getUnitId();
        this.name = unit.getUnitName();
        this.description = unit.getUnitDescription();
        this.organisation = new OrganisationReponse(unit.getOrganisation());
        }
    }
}
