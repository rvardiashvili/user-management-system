package org.example.employeemanagement.domain.bridge.Permission;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.Permission.id.CompanyPersonPermissionId;

@Entity
@Table(name = "company_person_permissions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class CompanyPersonPermission {

    @EmbeddedId
    private CompanyPersonPermissionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("companyId")
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public CompanyPersonPermission(Company company, Person person, Permission permission) {
        this.company = company;
        this.person = person;
        this.permission = permission;
        this.id = new CompanyPersonPermissionId(company.getCompanyId(), person.getPersonId(), permission.getId());
    }
}


