package org.example.employeemanagement.domain.bridge.group;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationGroupPersonId;

@Entity
@Table(name = "organisation_group_persons")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id"})
public class OrganisationGroupPerson {

    @EmbeddedId
    private OrganisationGroupPersonId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("personId")
    @JoinColumn(name = "person_id", nullable = false)
    private Person person; // Link to the company-scoped group

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("organisationId")
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organisation organisation; // Link to the company-scoped group


    public OrganisationGroupPerson(Group group, Person person, Organisation organisation, Permission permission) {
        this.group = group;
        this.person = person;
        this.organisation = organisation;
        this.id = new OrganisationGroupPersonId(group.getId(), person.getPersonId(), organisation.getOrganisationId());
    }
}

