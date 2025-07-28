package org.example.employeemanagement.dto.people;

import lombok.Data;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.bridge.Permission.CompanyPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.OrganisationPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.OrganisationUnitPersonPermission;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.bridge.role.OrganisationalUnitPersonRole;
import org.example.employeemanagement.domain.people.Address;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.Position;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.AllRolesResponse;
import org.example.employeemanagement.dto.GroupResponse;
import org.example.employeemanagement.dto.PositionResponse;
import org.example.employeemanagement.dto.people.requests.PositionRequest;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Set<PositionResponse> positions;
    private Set<CompanyPersonPermission> companyPermissions;
    private Set<OrganisationPersonPermission> organisationPermissions;
    private Set<OrganisationUnitPersonPermission> unitPermissions;
    private Set<Address> addresses;
    private AllRolesResponse allRoles;
    public UserResponse(){}

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            this.firstName = user.getPerson().getFirstName();
            this.lastName = user.getPerson().getLastName();
            if (user.getPerson().getDateOfBirth() != null) {
                this.dateOfBirth = user.getPerson().getDateOfBirth().toString();
            }
            this.companyPermissions = user.getPerson().getCompanyPermissions();
            this.organisationPermissions = user.getPerson().getOrganizationPermissions();
            this.unitPermissions = user.getPerson().getUnitPermissions();
            this.addresses = user.getPerson().getAddresses();
            this.positions = user.getPerson().getPositions().stream().map(PositionResponse::new).collect(Collectors.toSet());
            this.allRoles = new AllRolesResponse(user.getPerson().getCompanyRoles(), user.getPerson().getOrganizationRoles(), user.getPerson().getUnitRoles());
        }

    }

    public UserResponse(UserDetails userDetails) {
        this.userId = userDetails.getUserId();
        this.email = userDetails.getEmail();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();

        }
}