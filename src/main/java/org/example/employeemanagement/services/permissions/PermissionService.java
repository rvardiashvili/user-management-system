package org.example.employeemanagement.services.permissions;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.domain.scoped.ScopedPersonRole;
import org.example.employeemanagement.dto.GenericResponse;
import lombok.RequiredArgsConstructor;

import org.example.employeemanagement.repositories.entities.CompanyRepository;
import org.example.employeemanagement.repositories.entities.OrganisationRepository;
import org.example.employeemanagement.repositories.entities.OrganisationalUnitRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import org.example.employeemanagement.repositories.permission.CompanyPersonPermissionRepository;
import org.example.employeemanagement.repositories.permission.OrganisationPersonPermissionRepository;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.permission.UnitPersonPermissionRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ScopeFactory scopeFactory;

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    private User saveUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
        return user;
    }

    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName) // Assuming findByPermissionName
                .orElseThrow(() -> new UsernameNotFoundException("Permission not found: " + permissionName));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Transactional
    public GenericResponse givePermission(Long userId, String permissionName, String scopeType, Long scopeId, String currentUserEmail) {
        User user = getUserById(userId);
        Permission permission = getPermissionByName(permissionName);
        Person person = user.getPerson();
        User currentUser = getUserByEmail(currentUserEmail);


        if (person == null) {
            return new GenericResponse(400, "User has no associated person record.");
        }

            Optional<? extends ScopedPersonRole> currentPersonScopedRoleOptional;
            Optional<? extends ScopedPersonRole> personToModifyCurrentScopedRoleOptional;
            Scope scope = scopeFactory.createScope(scopeType, scopeId);

            if(!scope.hasScopedPermission(currentUser, getPermissionByName("permission"))){
                return new GenericResponse(401, "you don't have permission to modify permission.");
            }
            if(!scope.hasScopedPermission(currentUser, permission)) {
                return new GenericResponse(401, "cannot give permission you do not posses");
            }

            return scope.givePermission(user, permission);

    }



    @Transactional
    public GenericResponse deletePermission(Long userId, String permissionName, String scopeType, Long scopeId, String currentUserEmail) {
        User user = getUserById(userId);
        Permission permission = getPermissionByName(permissionName);
        User currentUser = getUserByEmail(currentUserEmail);
        Scope scope;
            try {
                scope = scopeFactory.createScope(scopeType, scopeId);
            }catch (UsernameNotFoundException e){
                return new GenericResponse(401, "invalid scope: " + e.getMessage());
            }
            if(!scope.hasScopedPermission(currentUser, getPermissionByName("permission"))){
                return new GenericResponse(401, "you don't have permission to delete permission.");
            }
            if(!scope.hasScopedPermission(currentUser, permission)) {
                return new GenericResponse(401, "cannot delete permission you do not posses");
            }
            return  scope.deletePermission(user, permission);
    }
}
