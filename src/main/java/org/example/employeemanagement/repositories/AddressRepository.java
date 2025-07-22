package org.example.employeemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.employeemanagement.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}