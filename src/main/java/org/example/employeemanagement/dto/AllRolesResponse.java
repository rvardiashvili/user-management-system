package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationalUnitPersonRole;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AllRolesResponse {
    Map<Long, Role> companyRoles;
    Map<Long, Role> organisationRoles;
    Map<Long, Role> unitRoles;

    AllRolesResponse() {}
    public AllRolesResponse(Set<CompanyPersonRole> cpr, Set<OrganisationPersonRole> opr, Set<OrganisationalUnitPersonRole> upr) {
        if(cpr != null)
            this.companyRoles = cpr.stream()
                .collect(Collectors.toMap(
                        companyPersonRole -> companyPersonRole.getCompany().getCompanyId(),
                        CompanyPersonRole::getRole
                ));
        if(opr != null)
            this.organisationRoles = opr.stream()
                .collect(Collectors.toMap(
                        organisationPersonRole -> organisationPersonRole.getOrganisation().getOrganisationId(),
                        OrganisationPersonRole::getRole
                ));
        if(upr != null)
            this.unitRoles = upr.stream()
                .collect(Collectors.toMap(
                        unitPersonRole -> unitPersonRole.getUnit().getUnitId(),
                        OrganisationalUnitPersonRole::getRole
                ));
    }
}
