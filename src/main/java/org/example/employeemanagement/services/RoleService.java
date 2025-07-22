package org.example.employeemanagement.services;

import org.example.employeemanagement.domain.Permission;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.domain.User;
import org.example.employeemanagement.dto.*;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.PermissionRepository;
import org.example.employeemanagement.repositories.RoleRepository;
import org.example.employeemanagement.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + roleName));
    }
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }
    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + permissionName));
    }

    @Transactional
    public GenericResponse changeRole(Long userId, String role_name,  Collection<? extends GrantedAuthority> authorities) {
        User user = getUserById(userId);
        Set<Permission> permissions = user.getPerson().getPermissions();

        if(authorities.contains(getPermissionByName("ROLE_admin"))){
            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
            userRepository.save(user);
            return new GenericResponse(200, "user role changed");
        }

        if(user.getPerson().getRole().getName() == "admin" || user.getPerson().getRole().getName() =="moderator"){
            return new GenericResponse(403, "only admin can change roles for moderators and admins");
        }
        if(role_name == "employee" && authorities.contains(getPermissionByName("add-user"))){
            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
        }
        if(role_name == "user" && authorities.contains(getPermissionByName("add-user"))){
            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
        }
        userRepository.save(user);
        return new GenericResponse(200, "user role changed");
    }

}
