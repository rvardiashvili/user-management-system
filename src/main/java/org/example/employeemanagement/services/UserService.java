package org.example.employeemanagement.services;

import jakarta.validation.Valid;
import org.example.employeemanagement.domain.Person;
import org.example.employeemanagement.domain.User;
import org.example.employeemanagement.dto.*;
import org.example.employeemanagement.repositories.RoleRepository;
import org.example.employeemanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.employeemanagement.domain.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserResponse getMyDetails(String email) {
        User user = getUserByEmail(email);
        Person person = user.getPerson();

        UserResponse dto = new UserResponse();
        dto.setUserId(user.getUserId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setDateOfBirth(person.getDateOfBirth().toString());
        dto.setRoles(person.getRoles());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllWithPerson().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateMyDetails(String email, UpdatePersonRequest request) {
        User user = getUserByEmail(email);
        Person person = user.getPerson();

        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(user);

        return getMyDetails(email);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Incorrect old password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
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

        // Update email if provided
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

        if (request.getRoles() != null) {
            Set<Role> newRoles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                newRoles.add(role);
            }
            person.setRoles(newRoles);
        }

        User updatedUser = userRepository.save(user);
        return new UserDetails(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserDetails getUserDetails(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return new UserDetails(user);
    }
}
