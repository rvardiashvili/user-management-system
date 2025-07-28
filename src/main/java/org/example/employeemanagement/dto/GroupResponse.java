package org.example.employeemanagement.dto;

import lombok.Data;
import org.example.employeemanagement.domain.authorities.Group;
import org.example.employeemanagement.domain.authorities.Permission;

import java.util.Set;

@Data
public class GroupResponse {
    private String name;
    private Set<Permission> permissions;

    GroupResponse() {}

    public GroupResponse(Group group) {
        this.name = group.getName();
        this.permissions = group.getPermissions();
    }
}
