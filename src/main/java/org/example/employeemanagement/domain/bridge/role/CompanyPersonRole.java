package org.example.employeemanagement.domain.bridge.role;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.CompanyPersonRoleId;

@Entity
@Table(name = "company_person_roles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class CompanyPersonRole implements ScopedPersonRole {

    @EmbeddedId
    private CompanyPersonRoleId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("companyId")
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    public CompanyPersonRole(Company company, Person person, Role role) {
        this.company = company;
        this.person = person;
        this.role = role;
        this.id = new CompanyPersonRoleId(company.getCompanyId(), person.getPersonId(), role.getId());
    }
}

