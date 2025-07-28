package org.example.employeemanagement.domain.bridge.Permission;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationPersonPermissionId;
import org.example.employeemanagement.domain.scoped.ScopedPersonPermission;

@Entity
@Table(name = "organisation_person_permissions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationPersonPermission implements ScopedPersonPermission {

    @EmbeddedId
    private OrganisationPersonPermissionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("organisationId")
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

    public OrganisationPersonPermission(Organisation organisation, Person person, Permission permission) {
        this.organisation = organisation;
        this.person = person;
        this.permission = permission;
        this.id = new OrganisationPersonPermissionId(organisation.getOrganisationId(), person.getPersonId(), permission.getId());
    }
}


