package org.example.employeemanagement.repositories.permission;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.bridge.Permission.OrganisationPersonPermission;
import org.example.employeemanagement.domain.bridge.Permission.id.OrganisationPersonPermissionId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonPermission;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganisationPersonPermissionRepository extends JpaRepository<OrganisationPersonPermission, OrganisationPersonPermissionId> {
}
