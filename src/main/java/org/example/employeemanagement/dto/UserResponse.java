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
    private String roleName;
    private Position position;
    private Set<Permission> permissions;
    private Set<Address> addresses;
    private Set<Group> groups;

    public UserResponse(){}

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        if (user.getPerson() != null) {
            this.firstName = user.getPerson().getFirstName();
            this.lastName = user.getPerson().getLastName();
            this.role = user.getPerson().getRole();
            this.roleName = user.getPerson().getRole().getName();
            if (user.getPerson().getDateOfBirth() != null) {
                this.dateOfBirth = user.getPerson().getDateOfBirth().toString();
            }
            this.permissions = user.getPerson().getPermissions();
            this.addresses = user.getPerson().getAddresses();
            this.position = user.getPerson().getPosition();
            this.groups = user.getPerson().getGroups();
        }
    }

    public UserResponse(UserDetails userDetails) {
        this.userId = userDetails.getUserId();
        this.email = userDetails.getEmail();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.roleName = userDetails.getRole();
        this.position = userDetails.getPosition();
        }
}