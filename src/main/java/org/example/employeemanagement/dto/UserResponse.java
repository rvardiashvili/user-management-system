package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.*;

import java.util.Set;

@Data
public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private Role role;
    private Set<Permission> permissions;
    private Set<Address> addresses;
    public UserResponse(){}

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            this.firstName = user.getPerson().getFirstName();
            this.lastName = user.getPerson().getLastName();
            this.role = user.getPerson().getRole();
            if (user.getPerson().getDateOfBirth() != null) {
                this.dateOfBirth = user.getPerson().getDateOfBirth().toString();
            }
            this.permissions = user.getPerson().getPermissions();
            this.addresses = user.getPerson().getAddresses();
        }
    }

}