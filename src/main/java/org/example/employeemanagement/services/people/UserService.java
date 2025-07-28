package org.example.employeemanagement.services.people;

import org.example.employeemanagement.domain.authorities.Permission;
import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.people.*;
import org.example.employeemanagement.dto.people.requests.AdminUserUpdateRequest;
import org.example.employeemanagement.dto.people.requests.ChangeEmailRequest;
import org.example.employeemanagement.dto.people.requests.ChangePasswordRequest;
import org.example.employeemanagement.dto.people.requests.UpdatePersonRequest;
import org.example.employeemanagement.utils.Scope;
import org.example.employeemanagement.utils.ScopeFactory;
import org.example.employeemanagement.repositories.permission.PermissionRepository;
import org.example.employeemanagement.repositories.role.RoleRepository;
import org.example.employeemanagement.repositories.people.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ScopeFactory scopeFactory;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }


    private Permission getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName) // Assuming findByPermissionName
                .orElseThrow(() -> new UsernameNotFoundException("Permission not found: " + permissionName));
    }

    @Transactional(readOnly = true)
    public UserResponse getDetails(String email, String scope_type, Long scope_id, String current_user_email) {

        User user = getUserByEmail(email);
        User current_user = getUserByEmail(current_user_email);
        Permission permission = getPermissionByName("view-users");
        Scope scope = scopeFactory.createScope(scope_type, scope_id);
        if(scope.hasScopedPermission(current_user, permission))
            return new UserResponse(user);
        else
            return null;
    }

    public UserResponse getDetailsSelf(String email) {
        User user = getUserByEmail(email);
        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getDetails(Long id, String scope_type, Long scope_id,  String current_user_email) {

        User user = getUserById(id);
        User current_user = getUserByEmail(current_user_email);
        Permission permission = getPermissionByName("view-users");
        Scope scope = scopeFactory.createScope(scope_type, scope_id);
        user.getPerson().getCompanyRoles().size();
        user.getPerson().getOrganizationRoles().size();
        user.getPerson().getUnitRoles().size();
        System.out.println(user.getPerson().getCompanyRoles());
        System.out.println("kala");
        if(scope.hasScopedPermission(current_user, permission))
            return new UserResponse(user);
        else
            return null;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(String scope_type, Long scope_id, String current_user_email) {
        User user = getUserByEmail(current_user_email);
        Scope scope = scopeFactory.createScope(scope_type, scope_id);
        System.out.println(scope);
        Permission view_permission = getPermissionByName("view-users");
        System.out.println(scope.hasScopedPermission(user, view_permission));
        if(scope.hasScopedPermission(user, view_permission))
            return scope.findAllUsers();
        else{
            throw new UsernameNotFoundException("user has no view-user permission in this scope: " + current_user_email);
        }
    }

    @Transactional
    public UserResponse updateMyDetails(String email, UpdatePersonRequest request) {
        User user = getUserByEmail(email);
        Person person = user.getPerson();
        if(request.getFirstName() != null)
            person.setFirstName(request.getFirstName());
        if(request.getLastName() != null)
            person.setLastName(request.getLastName());
        if(request.getDateOfBirth() != null)
            person.setDateOfBirth(request.getDateOfBirth());

        try {
        userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }

        return new UserResponse(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Incorrect old password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        try {
            userRepository.save(user);
        }catch (DataAccessException e){
            throw e;
        }
    }

    @Transactional
    public void changeEmail(String email, ChangeEmailRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Incorrect password");
        }

        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new IllegalStateException("New email is already in use");
        }

        user.setEmail(request.getNewEmail());
        userRepository.save(user);
    }

    @Transactional
    public UserDetails updateUserDetails(Long userId, AdminUserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        Person person = user.getPerson();

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalStateException("Email already in use by another account.");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            person.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            person.setLastName(request.getLastName());
        }

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            user.setStatus(request.getStatus());
        }

        User updatedUser = userRepository.save(user);
        return new UserDetails(updatedUser);
    }



}
