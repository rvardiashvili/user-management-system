package org.example.employeemanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.employeemanagement.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}