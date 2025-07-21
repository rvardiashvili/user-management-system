package org.example.employeemanagement.services;

import org.example.employeemanagement.domain.Person;
import org.example.employeemanagement.domain.User;
import org.example.employeemanagement.dto.SignUpRequest;
import org.example.employeemanagement.dto.UserResponse;
import org.example.employeemanagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.employeemanagement.domain.Role;
import org.example.employeemanagement.repositories.RoleRepository;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalStateException("Email already in use!");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setStatus("active");

        Person person = new Person();
        person.setFirstName(signUpRequest.getFirstName());
        person.setLastName(signUpRequest.getLastName());
        person.setUser(user);
        Role userRole = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
        person.setRoles(Set.of(userRole));
        user.setPerson(person);

        User savedUser = userRepository.save(user);

        UserResponse responseDto = new UserResponse();
        responseDto.setUserId(savedUser.getUserId());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setFirstName(savedUser.getPerson().getFirstName());
        responseDto.setLastName(savedUser.getPerson().getLastName());

        return responseDto;
    }
}