package org.example.employeemanagement.repositories.role;

import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.CompanyPersonRoleId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyPersonRoleRepository extends JpaRepository<CompanyPersonRole, CompanyPersonRoleId> {
    Optional<? extends ScopedPersonRole> findByCompanyAndPerson(Company company, Person currentPerson);

    void delete(ScopedPersonRole scopedPersonRole);
}
