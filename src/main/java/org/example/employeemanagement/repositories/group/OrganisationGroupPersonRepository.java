package org.example.employeemanagement.repositories.group;

import org.example.employeemanagement.domain.bridge.group.OrganisationGroupPerson;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationGroupPersonId;
import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationPersonRoleId;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationGroupPersonRepository extends JpaRepository<OrganisationGroupPerson, OrganisationGroupPersonId> {
    List<OrganisationGroupPerson> findByOrganisationAndPerson(Organisation organisation, Person person);
}
