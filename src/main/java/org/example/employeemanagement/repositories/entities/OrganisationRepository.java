package org.example.employeemanagement.repositories.entities;

import org.example.employeemanagement.domain.entities.Company;
import org.example.employeemanagement.domain.entities.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    Optional<List<Organisation>> findByCompany(Company company);
}
