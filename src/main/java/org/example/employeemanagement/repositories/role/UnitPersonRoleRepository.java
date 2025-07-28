package org.example.employeemanagement.repositories.role;

import org.example.employeemanagement.domain.bridge.role.OrganisationalUnitPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.OrganisationalUnitPersonRoleId;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitPersonRoleRepository extends JpaRepository<OrganisationalUnitPersonRole, OrganisationalUnitPersonRoleId> {
    Optional<? extends ScopedPersonRole> findByUnitAndPerson(OrganisationalUnit unit, Person currentPerson);

    void delete(ScopedPersonRole scopedPersonRole);
}
