package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.Permission;
import org.example.employeemanagement.domain.Person;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.domain.User;

import java.util.Set;

@Data
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Set<Role> roles;
    private Set<Permission> permissions;
    public UserResponse(){}

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            this.firstName = user.getPerson().getFirstName();
            this.lastName = user.getPerson().getLastName();
            this.roles = user.getPerson().getRoles();
            if (user.getPerson().getDateOfBirth() != null) {
                this.dateOfBirth = user.getPerson().getDateOfBirth().toString();
            }
            this.permissions = user.getPerson().getPermissions();
        }
    }

}