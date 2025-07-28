package org.example.employeemanagement.repositories.permission;

import org.example.employeemanagement.domain.bridge.Permission.OrganisationUnitPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationUnitPersonPermissionId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonPermission;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitPersonPermissionRepository extends JpaRepository<OrganisationUnitPersonPermission, OrganisationUnitPersonPermissionId> {
    Optional<? extends ScopedPersonPermission> findByUnitAndPerson(OrganisationalUnit unit, Person currentPerson);

}
