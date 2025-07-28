package org.example.employeemanagement.domain.bridge.group;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.group.id.CompanyGroupPersonId;

@Entity
@Table(name = "company_group_persons")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class CompanyGroupPerson {

    @EmbeddedId
    private CompanyGroupPersonId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("companyId")
    @JoinColumn(name = "company_id", nullable = false)
    private Company company; // Link to the company-scoped group


    public CompanyGroupPerson(Group group, Person person, Company company, Permission permission) {
        this.group = group;
        this.person = person;
        this.company = company;
        this.id = new CompanyGroupPersonId(group.getId(), person.getPersonId(), company.getCompanyId());
    }
}

