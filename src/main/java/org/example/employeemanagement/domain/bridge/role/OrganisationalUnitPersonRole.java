package org.example.employeemanagement.domain.bridge.role;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationalUnitPersonRoleId;

@Entity
@Table(name = "unit_person_roles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationalUnitPersonRole implements ScopedPersonRole {

    @EmbeddedId
    private OrganisationalUnitPersonRoleId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("organisationalUnitId")
    @JoinColumn(name = "unit_id", nullable = false)
    private OrganisationalUnit unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    public OrganisationalUnitPersonRole(OrganisationalUnit unit, Person person, Role role) {
        this.unit = unit;
        this.person = person;
        this.role = role;
        this.id = new OrganisationalUnitPersonRoleId(unit.getUnitId(), person.getPersonId(), role.getId());
    }
}

