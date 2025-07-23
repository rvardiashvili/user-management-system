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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


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

    @Transactional
    public GenericResponse changeRole(Long userId, String role_name,  Collection<? extends GrantedAuthority> authorities) {
        User user = getUserById(userId);
        Set<String> authoritiesSet = authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        System.out.println("Role added:12321 ");

        if(authoritiesSet.contains("ROLE_admin")){
            System.out.println("Role added:admining ");

            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
            try {
            userRepository.save(user);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            return new GenericResponse(200, "user role changed");
        }
        System.out.println("Role added:12321 ");

        if(Objects.equals(user.getPerson().getRole().getName(), "admin") || Objects.equals(user.getPerson().getRole().getName(), "moderator")){
            return new GenericResponse(403, "only admin can change roles for moderators and admins");
        }
        System.out.println(authoritiesSet.contains("add-users"));
        System.out.println(authoritiesSet);
        System.out.println(role_name.equals("employee") && authoritiesSet.contains("add-users"));
        if(role_name.equals("employee") && authoritiesSet.contains("add-users")){
            System.out.println("Role added: " + "employee");
            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
        }
        if(role_name.equals("user") && authoritiesSet.contains("remove-users")){
            Role newrole = getRoleByName(role_name);
            user.getPerson().setRole(newrole);
        }
        try {
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return new GenericResponse(200, "user role changed");
    }

}
