package org.example.employeemanagement.domain.bridge.Permission;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationUnitPersonPermissionId;
import org.example.employeemanagement.domain.scoped.ScopedPersonPermission;

@Entity
@Table(name = "unit_person_permissions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationUnitPersonPermission implements ScopedPersonPermission {

    @EmbeddedId
    private OrganisationUnitPersonPermissionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("unitId")
    @JoinColumn(name = "unit_id", nullable = false)
    private OrganisationalUnit unit;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public OrganisationUnitPersonPermission(OrganisationalUnit unit, Person person, Permission permission) {
        this.unit = unit;
        this.person = person;
        this.permission = permission;
        this.id = new OrganisationUnitPersonPermissionId(unit.getUnitId(), person.getPersonId(), permission.getId());
    }
}


