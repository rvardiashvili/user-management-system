package org.example.employeemanagement.repositories.entities;

import org.example.employeemanagement.domain.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Object> findByCompanyName(String companyName);
}
