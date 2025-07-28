package org.example.employeemanagement.services.people;

import org.example.employeemanagement.domain.people.Person;
import org.example.employeemanagement.domain.people.User;
import org.example.employeemanagement.dto.people.requests.SignUpRequest;
import org.example.employeemanagement.dto.people.UserResponse;
import org.example.employeemanagement.repositories.people.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.employeemanagement.repositories.role.RoleRepository;

import  java.time.LocalDateTime;

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
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Person person = new Person();
        person.setFirstName(signUpRequest.getFirstName());
        person.setLastName(signUpRequest.getLastName());
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        user.setPerson(person);
        person.setUser(user);

        UserResponse responseDto = new UserResponse();
        try {
            User savedUser = userRepository.save(user);
            responseDto.setUserId(savedUser.getUserId());
            responseDto.setEmail(savedUser.getEmail());
            responseDto.setFirstName(savedUser.getPerson().getFirstName());
            responseDto.setLastName(savedUser.getPerson().getLastName());

        }catch (DataAccessException e){
            throw e;
        }

        return responseDto;
    }
}