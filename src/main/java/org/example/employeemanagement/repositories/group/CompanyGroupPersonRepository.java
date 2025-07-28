package org.example.employeemanagement.repositories.group;

import org.example.employeemanagement.domain.bridge.group.CompanyGroupPerson;
import org.example.employeemanagement.domain.bridge.group.id.CompanyGroupPersonId;
import org.example.employeemanagement.domain.bridge.role.CompanyPersonRole;
import org.example.employeemanagement.domain.bridge.role.id.CompanyPersonRoleId;
import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyGroupPersonRepository extends JpaRepository<CompanyGroupPerson, CompanyGroupPersonId> {
    List<CompanyGroupPerson> findByCompanyAndPerson(Company company, Person person);
}
