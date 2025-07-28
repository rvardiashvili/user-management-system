package org.example.employeemanagement.domain.bridge.group;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationalUnitGroupPersonId;

@Entity
@Table(name = "unit_group_persons")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationalUnitGroupPerson {

    @EmbeddedId
    private OrganisationalUnitGroupPersonId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("unitId")
    @JoinColumn(name = "unit_id", nullable = false)
    private OrganisationalUnit unit; // Link to the company-scoped group


    public OrganisationalUnitGroupPerson(Group group, Person person, OrganisationalUnit unit) {
        this.group = group;
        this.person = person;
        this.unit = unit;
        this.id = new OrganisationalUnitGroupPersonId(group.getId(), person.getPersonId(), unit.getUnitId());
    }
}

