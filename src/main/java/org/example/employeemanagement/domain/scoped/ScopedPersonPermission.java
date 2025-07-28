package org.example.employeemanagement.domain.scoped;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;

public interface ScopedPersonPermission {
    Person getPerson();
    Permission getPermission();
}
