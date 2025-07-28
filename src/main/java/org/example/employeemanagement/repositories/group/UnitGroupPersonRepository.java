package org.example.employeemanagement.repositories.group;

import org.example.employeemanagement.domain.bridge.group.OrganisationalUnitGroupPerson;
import org.example.employeemanagement.domain.bridge.group.id.OrganisationalUnitGroupPersonId;
import org.example.employeemanagement.domain.entities.OrganisationalUnit;
import org.example.employeemanagement.domain.people.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitGroupPersonRepository extends JpaRepository<OrganisationalUnitGroupPerson, OrganisationalUnitGroupPersonId> {
    List<OrganisationalUnitGroupPerson> findByUnitAndPerson(OrganisationalUnit unit, Person person);
}

