package org.example.employeemanagement.repositories.role;

import org.example.employeemanagement.domain.bridge.role.OrganisationPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationPersonRoleId;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationPersonRoleRepository extends JpaRepository<OrganisationPersonRole, OrganisationPersonRoleId> {
    Optional<? extends ScopedPersonRole> findByOrganisationAndPerson(Organisation organisation, Person currentPerson);

    void delete(ScopedPersonRole scopedPersonRole);
}
