package org.example.employeemanagement.repositories.group;

import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
    List<Group> findAllByCompany(Company company);
}