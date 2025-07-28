package org.example.employeemanagement.domain.bridge.role;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.authorities.Role;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationPersonRoleId;

@Entity
@Table(name = "organisation_person_roles")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationPersonRole implements ScopedPersonRole {

    @EmbeddedId
    private OrganisationPersonRoleId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("organisationId")
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    public OrganisationPersonRole(Organisation organisation, Person person, Role role) {
        this.organisation = organisation;
        this.person = person;
        this.role = role;
        this.id = new OrganisationPersonRoleId(organisation.getOrganisationId(), person.getPersonId(), role.getId());
    }
}

