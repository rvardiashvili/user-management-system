package org.example.employeemanagement.domain.scoped;

import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.authorities.Role;

public interface ScopedPersonRole {
    Person getPerson();
    Role getRole();
}
