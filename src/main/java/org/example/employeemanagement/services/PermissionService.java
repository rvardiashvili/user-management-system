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
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new UsernameNotFoundException("Role not found: " + roleName));
    }
    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }
    private User saveUser(User user) {
        return userRepository.save(user);
    }
    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + permissionName));
    }

    @Transactional
    public PermissionResponse givePermission(Long userId, String permission_name,  Collection<? extends GrantedAuthority> authorities) {
            PermissionResponse permissionResponse = new PermissionResponse();
            User user = getUserById(userId);
            Set<Permission> permissions = user.getPerson().getPermissions();
            Permission permission = getPermissionByName(permission_name);
            Set<String> auth = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
            if (auth.contains(permission_name)){
                permissions.add(permission);
                saveUser(user);
                permissionResponse.setPermissions(permissions);
                permissionResponse.setUserId(userId);
                permissionResponse.setEmail(user.getEmail());
            }
        return permissionResponse;
    }

    @Transactional
    public PermissionResponse deletePermission(Long userId, String permission_name, String email) {
        PermissionResponse permissionResponse = new PermissionResponse();
        Permission permission = getPermissionByName(permission_name);
        User user = getUserById(userId);
        User current_user = getUserByEmail(email);
        Role mod = getRoleByName("moderator");
        Role admin = getRoleByName("admin");
        if(user.getPerson().getRole() == mod){
            if(current_user.getPerson().getRole() != admin){
                return permissionResponse;
            }
        }
        if(user.getPerson().getRole() == admin){
            return permissionResponse;
        }
        if(!(current_user.getPerson().getRole() == admin || current_user.getPerson().getRole() == mod)){
            return permissionResponse;
        }
        Set<Permission> permissions = user.getPerson().getPermissions();
        permissions.remove(permission);
        saveUser(user);
        permissionResponse.setPermissions(permissions);
        permissionResponse.setUserId(userId);
        permissionResponse.setEmail(user.getEmail());
        return permissionResponse;
    }

}
